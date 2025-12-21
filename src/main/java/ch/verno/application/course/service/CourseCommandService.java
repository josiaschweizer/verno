package ch.verno.application.course.service;

import ch.verno.application.course.command.CreateCourseCommand;
import ch.verno.domain.model.course.Course;
import ch.verno.domain.repository.CourseRepositoryPort;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CourseCommandService {

  @Nonnull
  private final CourseRepositoryPort repository;

  public CourseCommandService(@Nonnull final CourseRepositoryPort repository) {
    this.repository = repository;
  }

  @Nonnull
  @Transactional
  public Course create(@Nonnull final CreateCourseCommand cmd) {
    final var course = new Course(
        0L,
        cmd.title(),
        cmd.capacity(),
        cmd.location(),
        cmd.durationMinutes(),
        cmd.courseLevelId(),
        cmd.instructorId(),
        cmd.courseUnitId(),
        Instant.now()
    );

    return repository.save(course);
  }
}