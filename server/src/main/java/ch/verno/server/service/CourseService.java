package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.service.ICourseService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
  public Optional<CourseDto> getCourseById(@Nonnull final Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll();
  }

}
