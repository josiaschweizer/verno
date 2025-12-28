package ch.verno.server.service;

import ch.verno.common.db.dto.*;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.*;
import ch.verno.server.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.*;

public class ServiceHelper {

  @Nullable
  public AddressEntity saveOrUpdateAddress(@Nonnull final AddressRepository addressRepository,
                                           @Nullable final AddressDto addressDto) {
    if (addressDto == null || addressDto.isEmpty()) {
      return null;
    }

    final AddressEntity entity;
    if (addressDto.getId() != null && addressDto.getId() != 0L) {
      entity = addressRepository.findById(addressDto.getId())
              .orElseThrow(() -> new NotFoundException(NotFoundReason.ADDRESS_BY_ID_NOT_FOUND, addressDto.getId()));

      entity.setStreet(safeString(addressDto.getStreet()));
      entity.setHouseNumber(safeString(addressDto.getHouseNumber()));
      entity.setZipCode(safeString(addressDto.getZipCode()));
      entity.setCity(safeString(addressDto.getCity()));
      entity.setCountry(safeString(addressDto.getCountry()));
    } else {
      entity = new AddressEntity(
              safeString(addressDto.getStreet()),
              safeString(addressDto.getHouseNumber()),
              safeString(addressDto.getZipCode()),
              safeString(addressDto.getCity()),
              safeString(addressDto.getCountry())
      );
    }

    return addressRepository.save(entity);
  }

  @Nullable
  public ParentEntity saveOrUpdateParent(@Nonnull final ParentRepository parentRepository,
                                         @Nonnull final GenderRepository genderRepository,
                                         @Nonnull final AddressRepository addressRepository,
                                         @Nullable final ParentDto parentDto) {
    if (parentDto == null || parentDto.isEmpty()) {
      return null;
    }

    final ParentEntity entity;
    if (parentDto.getId() != null && parentDto.getId() != 0) {
      entity = parentRepository.findById(parentDto.getId())
              .orElseThrow(() -> new NotFoundException(NotFoundReason.PARENT_BY_ID_NOT_FOUND, parentDto.getId()));
    } else {
      entity = new ParentEntity(
              safeString(parentDto.getFirstName()),
              safeString(parentDto.getLastName()),
              safeString(parentDto.getEmail()),
              !parentDto.getPhoneNumber().isEmpty()
                      ? parentDto.getPhoneNumber().toString()
                      : Publ.EMPTY_STRING
      );
    }

    entity.setFirstname(safeString(parentDto.getFirstName()));
    entity.setLastname(safeString(parentDto.getLastName()));
    entity.setEmail(safeString(parentDto.getEmail()));
    entity.setPhone(!parentDto.getPhoneNumber().isEmpty()
            ? parentDto.getPhoneNumber().toString()
            : Publ.EMPTY_STRING
    );

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
            .orElseThrow(() -> new NotFoundException(NotFoundReason.GENDER_BY_ID_NOT_FOUND, genderDto.getId()));
  }

  @Nullable
  public CourseLevelEntity resolveCourseLevel(@Nonnull final CourseLevelRepository courseLevelRepository,
                                              @Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return courseLevelRepository.findById(dto.getId())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, dto.getId()));
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
              .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, id));
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
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_SCHEDULE_BY_ID_NOT_FOUND, dto.getId()));
  }

  @Nullable
  public InstructorEntity resolveInstructor(@Nonnull final InstructorRepository instructorRepository,
                                            @Nullable final InstructorDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return instructorRepository.findById(dto.getId())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, dto.getId()));
  }

  @Nullable
  public CourseEntity resolveCourse(@Nonnull final CourseRepository courseRepository,
                                    @Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty() || dto.getId() == null) {
      return null;
    }

    return courseRepository.findById(dto.getId())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_BY_ID_NOT_FOUND, dto.getId()));
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
}