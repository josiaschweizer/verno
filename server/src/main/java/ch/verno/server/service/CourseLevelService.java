package ch.verno.server.service;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.server.mapper.CourseLevelMapper;
import ch.verno.server.repository.CourseLevelRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseLevelService implements ICourseLevelService {

  @Nonnull
  private final CourseLevelRepository courseLevelRepository;

  public CourseLevelService(@Nonnull final CourseLevelRepository courseLevelRepository) {
    this.courseLevelRepository = courseLevelRepository;
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public CourseLevelDto getCourseLevelById(@Nonnull final Long id) {
    final var foundById = courseLevelRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new IllegalArgumentException("Participant not found with id: " + id);
    }

    return CourseLevelMapper.toDto(foundById.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<CourseLevelDto> getAllCourseLevels() {
    return courseLevelRepository.findAll().stream().map(CourseLevelMapper::toDto).toList();
  }
}
