package ch.verno.server.service;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
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
  @Override
  @Transactional
  public CourseLevelDto createCourseLevel(@Nonnull final CourseLevelDto courseLevelDto) {
    final var entity = CourseLevelMapper.toEntity(courseLevelDto);
    if (entity == null) {
      throw new IllegalArgumentException("CourseLevelDto is empty");
    }

    entity.setId(null);

    final var saved = courseLevelRepository.save(entity);
    return CourseLevelMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public CourseLevelDto updateCourseLevel(@Nonnull final CourseLevelDto courseLevelDto) {
    if (courseLevelDto.getId() == null || courseLevelDto.getId() == 0) {
      throw new IllegalArgumentException("CourseLevel ID is required for update");
    }

    final var existing = courseLevelRepository.findById(courseLevelDto.getId())
            .orElseThrow(() -> new NotFoundException(
                    NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND,
                    courseLevelDto.getId()
            ));

    final var updated = CourseLevelMapper.toEntity(courseLevelDto);
    if (updated == null) {
      throw new IllegalArgumentException("CourseLevelDto is empty");
    }

    updated.setId(existing.getId());

    final var saved = courseLevelRepository.save(updated);
    return CourseLevelMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public CourseLevelDto getCourseLevelById(@Nonnull final Long id) {
    final var courseLevelOptional = courseLevelRepository.findById(id);
    if (courseLevelOptional.isEmpty()) {
      throw new NotFoundException(NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, id);
    }

    return CourseLevelMapper.toDto(courseLevelOptional.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<CourseLevelDto> getAllCourseLevels() {
    return courseLevelRepository.findAll().stream().map(CourseLevelMapper::toDto).toList();
  }
}
