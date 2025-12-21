package ch.verno.domain.repository;

import ch.verno.domain.model.course.CourseUnit;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface CourseUnitRepositoryPort {

  @Nonnull Optional<CourseUnit> findById(long id);

  @Nonnull CourseUnit save(@Nonnull CourseUnit unit);

  void deleteById(long id);
}