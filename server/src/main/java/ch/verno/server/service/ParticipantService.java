package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.ParticipantEntity;
import ch.verno.publ.Publ;
import ch.verno.server.mapper.CourseMapper;
import ch.verno.server.mapper.ParticipantMapper;
import ch.verno.server.repository.*;
import ch.verno.server.spec.PageHelper;
import ch.verno.server.spec.ParticipantSpec;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
  @Nonnull
  private final ParticipantSpec participantSpec;

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
    this.participantSpec = new ParticipantSpec();
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
                    : Publ.EMPTY_STRING,
            ServiceHelper.safeString(participantDto.getNote()),
            participantDto.isActive()
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
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.PARTICIPANT_BY_ID_NOT_FOUND, participantDto.getId()));

    existing.setFirstname(ServiceHelper.safeString(participantDto.getFirstName()));
    existing.setLastname(ServiceHelper.safeString(participantDto.getLastName()));
    existing.setBirthdate(participantDto.getBirthdate() != null ? participantDto.getBirthdate() : LocalDate.now());
    existing.setEmail(ServiceHelper.safeString(participantDto.getEmail()));
    existing.setPhone(!participantDto.getPhone().isEmpty()
            ? participantDto.getPhone().toString()
            : Publ.EMPTY_STRING
    );
    existing.setNote(ServiceHelper.safeString(participantDto.getNote()));
    existing.setActive(participantDto.isActive());

    final var savedParticipant = saveParticipant(participantDto, existing);
    return ParticipantMapper.toDto(savedParticipant);
  }

  @Nonnull
  private ParticipantEntity saveParticipant(@Nonnull final ParticipantDto participantDto,
                                            @Nonnull final ParticipantEntity existing) {
    existing.setGender(serviceHelper.resolveGender(genderRepository, participantDto.getGender()));
    existing.setCourseLevels(serviceHelper.resolveCourseLevels(courseLevelRepository, participantDto.getCourseLevels()));
    existing.setCourses(serviceHelper.resolveCourses(courseRepository, participantDto.getCourses()));

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
      throw new DBNotFoundException(DBNotFoundReason.PARTICIPANT_BY_ID_NOT_FOUND, id);
    }
    return ParticipantMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<ParticipantDto> getParticipantByEmail(@NonNull final String email) {
    final var participantByEmail = participantRepository.findByEmail(email);
    return participantByEmail.map(ParticipantMapper::toDto);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParticipantDto> getAllParticipants() {
    return participantRepository.findAll().stream()
            .map(ParticipantMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParticipantDto> findParticipants(@Nonnull final ParticipantFilter filter,
                                               final int offset,
                                               final int limit,
                                               @Nonnull final List<QuerySortOrder> sortOrders) {
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);
    final var spec = participantSpec.participantSpec(filter);

    return participantRepository.findAll(spec, pageable).stream()
            .map(ParticipantMapper::toDto)
            .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public int countParticipants(@Nonnull final ParticipantFilter filter) {
    return Math.toIntExact(participantRepository.count(participantSpec.participantSpec(filter)));
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParticipantDto> findParticipantsByCourse(@Nonnull final CourseDto course) {
    final var courseEntity = CourseMapper.toEntity(course);

    if (courseEntity == null) {
      return List.of();
    }

    return participantRepository.findByCourse(courseEntity)
            .stream()
            .map(ParticipantMapper::toDto)
            .toList();
  }
}