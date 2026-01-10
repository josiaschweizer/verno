package ch.verno.server.service;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.publ.Publ;
import ch.verno.db.entity.InstructorEntity;
import ch.verno.server.mapper.InstructorMapper;
import ch.verno.server.repository.AddressRepository;
import ch.verno.server.repository.GenderRepository;
import ch.verno.server.repository.InstructorRepository;
import ch.verno.server.spec.InstructorSpec;
import ch.verno.server.spec.PageHelper;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstructorService implements IInstructorService {

  @Nonnull
  private final InstructorRepository instructorRepository;
  @Nonnull
  private final AddressRepository addressRepository;
  @Nonnull
  private final GenderRepository genderRepository;

  @Nonnull
  private final ServiceHelper serviceHelper;
  @Nonnull
  private final InstructorSpec instructorSpec;

  public InstructorService(@Nonnull final InstructorRepository instructorRepository,
                           @Nonnull final AddressRepository addressRepository,
                           @Nonnull final GenderRepository genderRepository) {
    this.instructorRepository = instructorRepository;
    this.addressRepository = addressRepository;
    this.genderRepository = genderRepository;

    this.serviceHelper = new ServiceHelper();
    this.instructorSpec = new InstructorSpec();
  }

  @Nonnull
  @Override
  @Transactional
  public InstructorDto createInstructor(@Nonnull final InstructorDto instructorDto) {
    final var entity = new InstructorEntity(
            ServiceHelper.safeString(instructorDto.getFirstName()),
            ServiceHelper.safeString(instructorDto.getLastName()),
            ServiceHelper.safeString(instructorDto.getEmail()),
            !instructorDto.getPhone().isEmpty()
                    ? instructorDto.getPhone().toString()
                    : Publ.EMPTY_STRING
    );

    entity.setId(null);

    final var saved = saveInstructor(instructorDto, entity);
    return InstructorMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public InstructorDto updateInstructor(@Nonnull final InstructorDto instructorDto) {
    if (instructorDto.getId() == null || instructorDto.getId() == 0L) {
      throw new IllegalArgumentException("Instructor ID is required for update");
    }

    final var existing = instructorRepository.findById(instructorDto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, instructorDto.getId()));

    existing.setFirstname(ServiceHelper.safeString(instructorDto.getFirstName()));
    existing.setLastname(ServiceHelper.safeString(instructorDto.getLastName()));
    existing.setEmail(ServiceHelper.safeString(instructorDto.getEmail()));
    existing.setPhone(!instructorDto.getPhone().isEmpty()
            ? instructorDto.getPhone().toString()
            : Publ.EMPTY_STRING
    );

    final var saved = saveInstructor(instructorDto, existing);
    return InstructorMapper.toDto(saved);
  }

  @Nonnull
  private InstructorEntity saveInstructor(@Nonnull final InstructorDto instructorDto,
                                          @Nonnull final InstructorEntity existing) {
    existing.setGender(serviceHelper.resolveGender(genderRepository, instructorDto.getGender()));
    existing.setAddress(serviceHelper.saveOrUpdateAddress(addressRepository, instructorDto.getAddress()));
    return instructorRepository.save(existing);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public InstructorDto getInstructorById(@Nonnull final Long id) {
    final var foundById = instructorRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, id);
    }
    return InstructorMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<InstructorDto> getAllInstructors() {
    return instructorRepository.findAll().stream()
            .map(InstructorMapper::toDto)
            .toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<InstructorDto> findInstructors(@Nonnull final InstructorFilter filter,
                                             final int offset,
                                             final int limit,
                                             @Nonnull final List<QuerySortOrder> sortOrders) {
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);
    final var spec = instructorSpec.instructorSpec(filter);

    return instructorRepository.findAll(spec, pageable).stream()
            .map(InstructorMapper::toDto)
            .toList();
  }

  @Transactional(readOnly = true)
  public int countCourses(@Nonnull final InstructorFilter filter) {
    return Math.toIntExact(instructorRepository.count(instructorSpec.instructorSpec(filter)));
  }
}