package ch.verno.server.service;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.server.mapper.InstructorMapper;
import ch.verno.server.repository.InstructorRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InstructorService implements IInstructorService {

  @Nonnull
  private final InstructorRepository instructorRepository;

  public InstructorService(@Nonnull final InstructorRepository instructorRepository) {
    this.instructorRepository = instructorRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public InstructorDto createInstructor(@Nonnull final InstructorDto instructorDto) {
    final var entity = InstructorMapper.toEntity(instructorDto);
    if (entity == null) {
      throw new IllegalArgumentException("InstructorDto is empty");
    }

    entity.setId(null);

    final var saved = instructorRepository.save(entity);
    return InstructorMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public InstructorDto updateInstructor(@Nonnull final InstructorDto instructorDto) {
    final var id = instructorDto.id();
    if (id == null || id == 0L) {
      throw new IllegalArgumentException("InstructorDto.id must be set for update");
    }

    final var existing = instructorRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(NotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, id));

    InstructorMapper.updateEntity(existing, instructorDto);

    final var saved = instructorRepository.save(existing);
    return InstructorMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public InstructorDto getInstructorById(@Nonnull final Long id) {
    final var foundById = instructorRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.INSTRUCTOR_BY_ID_NOT_FOUND, id);
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
}