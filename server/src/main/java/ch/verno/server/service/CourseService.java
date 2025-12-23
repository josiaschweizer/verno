package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.service.ICourseService;
import ch.verno.server.mapper.CourseMapper;
import ch.verno.server.repository.CourseRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService implements ICourseService {

  @Nonnull
  private final CourseRepository courseRepository;

  public CourseService(@Nonnull final CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public CourseDto getCourseById(@Nonnull final Long id) {
    final var foundById = courseRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new IllegalArgumentException("Course not found with id: " + id);
    }

    return CourseMapper.toDto(foundById.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll().stream().map(CourseMapper::toDto).toList();
  }

}
