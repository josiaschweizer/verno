package ch.verno.common.db.service;

import ch.verno.server.repository.CourseRepository;
import ch.verno.server.entity.CourseEntity;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

  @Nonnull
  private final CourseRepository courseRepository;

  public CourseService(@Nonnull final CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  @Nonnull
  @Transactional(readOnly = true)
  public CourseEntity getCourseById(@Nonnull final Long id) {
    return courseRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseEntity> getAllCourses() {
    return courseRepository.findAll();
  }

}
