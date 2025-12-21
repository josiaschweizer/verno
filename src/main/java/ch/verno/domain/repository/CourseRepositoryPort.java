package ch.verno.domain.repository;

import ch.verno.domain.model.course.Course;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface CourseRepositoryPort {

  @Nonnull Optional<Course> findById(long id);

  @Nonnull Course save(@Nonnull Course course);

  void deleteById(long id);
}