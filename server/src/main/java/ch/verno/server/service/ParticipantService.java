package ch.verno.server.service;

import ch.verno.common.db.dto.AddressDto;
import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.db.dto.ParentDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.AddressEntity;
import ch.verno.db.entity.CourseEntity;
import ch.verno.db.entity.CourseLevelEntity;
import ch.verno.db.entity.GenderEntity;
import ch.verno.db.entity.ParentEntity;
import ch.verno.db.entity.ParticipantEntity;
import ch.verno.server.mapper.ParticipantMapper;
import ch.verno.server.repository.AddressRepository;
import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.repository.CourseRepository;
import ch.verno.server.repository.GenderRepository;
import ch.verno.server.repository.ParentRepository;
import ch.verno.server.repository.ParticipantRepository;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ParticipantService implements IParticipantService {

  @Nonnull
  private final ParticipantRepository participantRepository;
  @Nonnull
  private final AddressRepository addressRepository;
  @Nonnull
  private final ParentRepository parentRepository;
  @Nonnull
  private final GenderRepository genderRepository;
  @Nonnull
  private final CourseLevelRepository courseLevelRepository;
  @Nonnull
  private final CourseRepository courseRepository;

  public ParticipantService(@Nonnull final ParticipantRepository participantRepository,
                            @Nonnull final AddressRepository addressRepository,
                            @Nonnull final ParentRepository parentRepository,
                            @Nonnull final GenderRepository genderRepository,
                            @Nonnull final CourseLevelRepository courseLevelRepository,
                            @Nonnull final CourseRepository courseRepository) {
    this.participantRepository = participantRepository;
    this.addressRepository = addressRepository;
    this.parentRepository = parentRepository;
    this.genderRepository = genderRepository;
    this.courseLevelRepository = courseLevelRepository;
    this.courseRepository = courseRepository;
  }

  @Override
  @Transactional
  public @NonNull ParticipantDto createParticipant(@Nonnull final ParticipantDto participantDto) {
    final var entity = new ParticipantEntity(
        safeString(participantDto.getFirstName()),
        safeString(participantDto.getLastName()),
        participantDto.getBirthdate() != null ? participantDto.getBirthdate() : LocalDate.now(),
        safeString(participantDto.getEmail()),
        !participantDto.getPhone().isEmpty()
            ? participantDto.getPhone().toString()
            : Publ.EMPTY_STRING
    );

    final var savedParticipant = saveParticipant(participantDto, entity);
    return ParticipantMapper.toDto(savedParticipant);
  }

  @Override
  @Transactional
  public @NonNull ParticipantDto updateParticipant(@Nonnull final ParticipantDto participantDto) {
    if (participantDto.getId() == null || participantDto.getId() == 0) {
      throw new IllegalArgumentException("Participant ID is required for update");
    }

    final var existing = participantRepository.findById(participantDto.getId())
        .orElseThrow(() -> new NotFoundException(NotFoundReason.PARTICIPANT_BY_ID_NOT_FOUND, participantDto.getId()));

    existing.setFirstname(safeString(participantDto.getFirstName()));
    existing.setLastname(safeString(participantDto.getLastName()));
    existing.setBirthdate(participantDto.getBirthdate() != null ? participantDto.getBirthdate() : LocalDate.now());
    existing.setEmail(safeString(participantDto.getEmail()));
    existing.setPhone(!participantDto.getPhone().isEmpty()
        ? participantDto.getPhone().toString()
        : Publ.EMPTY_STRING
    );

    final var savedParticipant = saveParticipant(participantDto, existing);
    return ParticipantMapper.toDto(savedParticipant);
  }

  @Nonnull
  private ParticipantEntity saveParticipant(@Nonnull final ParticipantDto participantDto,
                                            @Nonnull final ParticipantEntity existing) {
    existing.setGender(resolveGender(participantDto.getGender()));
    existing.setCourseLevel(resolveCourseLevel(participantDto.getCourseLevel()));
    existing.setCourse(resolveCourse(participantDto.getCourse()));

    existing.setAddress(saveOrUpdateAddress(participantDto.getAddress()));
    existing.setParentOne(saveOrUpdateParent(participantDto.getParentOne()));
    existing.setParentTwo(saveOrUpdateParent(participantDto.getParentTwo()));

    return participantRepository.save(existing);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public ParticipantDto getParticipantById(@Nonnull final Long id) {
    final var foundById = participantRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.PARTICIPANT_BY_ID_NOT_FOUND, id);
    }
    return ParticipantMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParticipantDto> getAllParticipants() {
    return participantRepository.findAll().stream()
        .map(ParticipantMapper::toDto)
        .toList();
  }

  @Nullable
  private AddressEntity saveOrUpdateAddress(@Nullable final AddressDto addressDto) {
    if (addressDto == null || addressDto.isEmpty()) {
      return null;
    }

    final AddressEntity entity;
    if (addressDto.getId() != null && addressDto.getId() != 0) {
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
  private ParentEntity saveOrUpdateParent(@Nullable final ParentDto parentDto) {
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

    entity.setGender(resolveGender(parentDto.getGender()));
    entity.setAddress(saveOrUpdateAddress(parentDto.getAddress()));

    return parentRepository.save(entity);
  }

  @Nullable
  private GenderEntity resolveGender(@Nullable final GenderDto genderDto) {
    if (genderDto == null || genderDto.isEmpty() || genderDto.id() == null) {
      return null;
    }
    return genderRepository.findById(genderDto.id())
        .orElseThrow(() -> new NotFoundException(NotFoundReason.GENDER_BY_ID_NOT_FOUND, genderDto.id()));
  }

  @Nullable
  private CourseLevelEntity resolveCourseLevel(@Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty() || dto.id() == null) {
      return null;
    }
    return courseLevelRepository.findById(dto.id())
        .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, dto.id()));
  }

  @Nullable
  private CourseEntity resolveCourse(@Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty() || dto.id() == null) {
      return null;
    }
    return courseRepository.findById(dto.id())
        .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_BY_ID_NOT_FOUND, dto.id()));
  }

  @Nonnull
  private static String safeString(@Nullable final String value) {
    return value == null ? Publ.EMPTY_STRING : value;
  }
}