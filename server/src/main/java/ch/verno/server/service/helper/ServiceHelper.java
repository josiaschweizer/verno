package ch.verno.server.service.helper;

import ch.verno.common.db.dto.table.*;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.*;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.publ.Publ;
import ch.verno.common.mandant.MandantContext;
import ch.verno.server.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.*;

public class ServiceHelper {

  @Nullable
  public AddressEntity saveOrUpdateAddress(@Nonnull final AddressRepository addressRepository,
                                           @Nullable final AddressDto addressDto) {
    if (addressDto == null) {
      return null;
    }

    final var street = safeString(addressDto.getStreet()).trim();
    final var houseNumber = safeString(addressDto.getHouseNumber()).trim();
    final var zipCode = safeString(addressDto.getZipCode()).trim();
    final var city = safeString(addressDto.getCity()).trim();
    final var country = safeString(addressDto.getCountry()).trim();

    final boolean allBlank = street.isEmpty()
            && houseNumber.isEmpty()
            && zipCode.isEmpty()
            && city.isEmpty()
            && country.isEmpty();

    if (allBlank) {
      return null;
    }

    final AddressEntity entity;
    if (addressDto.getId() != null && addressDto.getId() != 0L) {
      entity = addressRepository.findById(addressDto.getId())
              .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.ADDRESS_BY_ID_NOT_FOUND, addressDto.getId()));

      entity.setStreet(street);
      entity.setHouseNumber(houseNumber);
      entity.setZipCode(zipCode);
      entity.setCity(city);
      entity.setCountry(country);
    } else {
      // entity constructors now require a MandantEntity first; use a null-id MandantEntity as fallback
      final var mandantId = MandantContext.getRequired();
      entity = new AddressEntity(MandantEntity.ref(mandantId), street, houseNumber, zipCode, city, country);
    }

    return addressRepository.save(entity);
  }

  @Nullable
  public ParentEntity saveOrUpdateParent(@Nonnull final ParentRepository parentRepository,
                                         @Nonnull final GenderRepository genderRepository,
                                         @Nonnull final AddressRepository addressRepository,
                                         @Nullable final ParentDto parentDto) {
    if (parentDto == null) {
      return null;
    }

    final var firstName = safeString(parentDto.getFirstName()).trim();
    final var lastName = safeString(parentDto.getLastName()).trim();
    final var email = safeTrimToNull(parentDto.getEmail());
    final var phone = safeTrimToNull(!parentDto.getPhone().isEmpty() ? parentDto.getPhone().toString() : null);

    final boolean noTextFields = firstName.isEmpty()
            && lastName.isEmpty()
            && email == null
            && phone == null;

    final boolean hasGender = !parentDto.getGender().isEmpty() &&
            parentDto.getGender().getId() != null &&
            parentDto.getGender().getId() != 0;

    final boolean hasAddress = hasAddressContent(parentDto.getAddress());

    if (noTextFields && !hasGender && !hasAddress) {
      return null;
    }

    final var mandantId = MandantContext.getRequired();

    final ParentEntity entity;
    if (parentDto.getId() != null && parentDto.getId() != 0L) {
      entity = parentRepository.findById(parentDto.getId(), mandantId)
              .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.PARENT_BY_ID_NOT_FOUND, parentDto.getId()));
    } else {
      // ParentEntity requires MandantEntity as first parameter
      entity = new ParentEntity(
              MandantEntity.ref(mandantId),
              firstName,
              lastName,
              email == null ? Publ.EMPTY_STRING : email,
              phone == null ? Publ.EMPTY_STRING : phone
      );
    }

    entity.setFirstname(firstName);
    entity.setLastname(lastName);
    entity.setEmail(email);
    entity.setPhone(phone == null ? Publ.EMPTY_STRING : phone);

    entity.setGender(resolveGender(genderRepository, parentDto.getGender()));
    entity.setAddress(saveOrUpdateAddress(addressRepository, parentDto.getAddress()));

    return parentRepository.save(entity);
  }

  @Nullable
  public GenderEntity resolveGender(@Nonnull final GenderRepository genderRepository,
                                    @Nullable final GenderDto genderDto) {
    if (genderDto == null || genderDto.isEmpty() || genderDto.getId() == null) {
      return null;
    }

    return genderRepository.findById(genderDto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.GENDER_BY_ID_NOT_FOUND, genderDto.getId()));
  }

  @Nullable
  public CourseLevelEntity resolveCourseLevel(@Nonnull final CourseLevelRepository courseLevelRepository,
                                              @Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return courseLevelRepository.findById(dto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, dto.getId()));
  }

  @Nonnull
  public List<CourseLevelEntity> resolveCourseLevels(@Nonnull final CourseLevelRepository courseLevelRepository,
                                                     @Nullable final List<CourseLevelDto> dtos) {
    if (dtos == null || dtos.isEmpty()) {
      return List.of();
    }

    final Map<Long, CourseLevelDto> uniqueByIdInOrder = new LinkedHashMap<>();
    for (final var dto : dtos) {
      if (dto == null || dto.isEmpty() || dto.getId() == null || dto.getId() == 0) {
        continue;
      }
      uniqueByIdInOrder.put(dto.getId(), dto);
    }

    if (uniqueByIdInOrder.isEmpty()) {
      return List.of();
    }

    final List<CourseLevelEntity> result = new ArrayList<>();
    for (final var id : uniqueByIdInOrder.keySet()) {
      final var entity = courseLevelRepository.findById(id)
              .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, id));
      result.add(entity);
    }

    return result;
  }

  @Nullable
  public CourseScheduleEntity resolveCourseSchedule(@Nonnull final CourseScheduleRepository courseScheduleRepository,
                                                    @Nullable final CourseScheduleDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return courseScheduleRepository.findById(dto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.COURSE_SCHEDULE_BY_ID_NOT_FOUND, dto.getId()));
  }

  @Nullable
  public InstructorEntity resolveInstructor(@Nonnull final InstructorRepository instructorRepository,
                                            @Nullable final InstructorDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return instructorRepository.findById(dto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, dto.getId()));
  }

  @Nonnull
  public List<CourseEntity> resolveCourses(@Nonnull final CourseRepository courseRepository,
                                           @Nullable final List<CourseDto> dtos) {
    if (dtos == null || dtos.isEmpty()) {
      return List.of();
    }

    final Map<Long, CourseDto> uniqueByIdInOrder = new LinkedHashMap<>();
    for (final var dto : dtos) {
      if (dto == null || dto.isEmpty() || dto.getId() == null || dto.getId() == 0) {
        continue;
      }
      uniqueByIdInOrder.put(dto.getId(), dto);
    }

    if (uniqueByIdInOrder.isEmpty()) {
      return List.of();
    }

    final List<CourseEntity> result = new ArrayList<>();
    for (final var id : uniqueByIdInOrder.keySet()) {
      final var entity = courseRepository.findById(id)
              .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.COURSE_BY_ID_NOT_FOUND, id));
      result.add(entity);
    }

    return result;
  }

  @Nonnull
  public Set<DayOfWeek> safeWeekdays(@Nullable final Set<DayOfWeek> weekdays) {
    if (weekdays == null || weekdays.isEmpty()) {
      return Collections.emptySet();
    }
    return Set.copyOf(new HashSet<>(weekdays));
  }

  @Nonnull
  public static String safeString(@Nullable final String value) {
    return value == null ? Publ.EMPTY_STRING : value;
  }

  @Nullable
  private static String safeTrimToNull(@Nullable final String value) {
    if (value == null) {
      return null;
    }
    final var trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }

  private static boolean hasAddressContent(@Nullable final AddressDto addressDto) {
    if (addressDto == null) {
      return false;
    }

    final var street = safeString(addressDto.getStreet()).trim();
    final var houseNumber = safeString(addressDto.getHouseNumber()).trim();
    final var zipCode = safeString(addressDto.getZipCode()).trim();
    final var city = safeString(addressDto.getCity()).trim();
    final var country = safeString(addressDto.getCountry()).trim();

    final boolean allBlank = street.isEmpty()
            && houseNumber.isEmpty()
            && zipCode.isEmpty()
            && city.isEmpty()
            && country.isEmpty();

    return !allBlank;
  }
}