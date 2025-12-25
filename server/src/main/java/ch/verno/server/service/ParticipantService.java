package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.CourseEntity;
import ch.verno.db.entity.CourseLevelEntity;
import ch.verno.db.entity.GenderEntity;
import ch.verno.db.entity.ParticipantEntity;
import ch.verno.server.mapper.ParticipantMapper;
import ch.verno.server.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
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

  @Nonnull
  private final ServiceHelper serviceHelper;

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

    this.serviceHelper = new ServiceHelper();
  }

  @Nonnull
  @Override
  @Transactional
  public ParticipantDto createParticipant(@Nonnull final ParticipantDto participantDto) {
    final var entity = new ParticipantEntity(
            ServiceHelper.safeString(participantDto.getFirstName()),
            ServiceHelper.safeString(participantDto.getLastName()),
        participantDto.getBirthdate() != null ? participantDto.getBirthdate() : LocalDate.now(),
            ServiceHelper.safeString(participantDto.getEmail()),
        !participantDto.getPhone().isEmpty()
            ? participantDto.getPhone().toString()
            : Publ.EMPTY_STRING
    );

    final var savedParticipant = saveParticipant(participantDto, entity);
    return ParticipantMapper.toDto(savedParticipant);
  }

  @Nonnull
  @Override
  @Transactional
  public ParticipantDto updateParticipant(@Nonnull final ParticipantDto participantDto) {
    if (participantDto.getId() == null || participantDto.getId() == 0) {
      throw new IllegalArgumentException("Participant ID is required for update");
    }

    final var existing = participantRepository.findById(participantDto.getId())
        .orElseThrow(() -> new NotFoundException(NotFoundReason.PARTICIPANT_BY_ID_NOT_FOUND, participantDto.getId()));

    existing.setFirstname(ServiceHelper.safeString(participantDto.getFirstName()));
    existing.setLastname(ServiceHelper.safeString(participantDto.getLastName()));
    existing.setBirthdate(participantDto.getBirthdate() != null ? participantDto.getBirthdate() : LocalDate.now());
    existing.setEmail(ServiceHelper.safeString(participantDto.getEmail()));
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
    existing.setGender(serviceHelper.resolveGender(genderRepository, participantDto.getGender()));
    existing.setCourseLevel(serviceHelper.resolveCourseLevel(courseLevelRepository, participantDto.getCourseLevel()));
    existing.setCourse(serviceHelper.resolveCourse(courseRepository, participantDto.getCourse()));

    existing.setAddress(serviceHelper.saveOrUpdateAddress(addressRepository, participantDto.getAddress()));
    existing.setParentOne(serviceHelper.saveOrUpdateParent(parentRepository, genderRepository, addressRepository, participantDto.getParentOne()));
    existing.setParentTwo(serviceHelper.saveOrUpdateParent(parentRepository, genderRepository, addressRepository, participantDto.getParentTwo()));

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
}