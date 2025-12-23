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
    final var courseLevelOptional = courseLevelRepository.findById(id);
    if (courseLevelOptional.isEmpty()) {
      throw new IllegalArgumentException("Course Level not found with id: " + id);
    }

    return CourseLevelMapper.toDto(courseLevelOptional.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<CourseLevelDto> getAllCourseLevels() {
    return courseLevelRepository.findAll().stream().map(CourseLevelMapper::toDto).toList();
  }
}
