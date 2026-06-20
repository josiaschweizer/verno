package ch.verno.db.jpa.course;

import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface SpringDataCourseJpaRepository extends AbstractEntityJpaRepository<CourseEntity, Long> {

  @Nonnull
  List<CourseEntity> findByCourseSchedule_Id(@Nonnull Long courseLevelId);

  boolean existsByInstructor_Id(@Nonnull Long instructorId);

}
