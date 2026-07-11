package ch.verno.db.jpa.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface SpringDataCourseScheduleJpaRepository extends AbstractEntityJpaRepository<CourseScheduleEntity, Long> {

  @Nonnull
  List<CourseScheduleEntity> findByWeeksContains(@Nonnull String week);

  @Nonnull
  List<CourseScheduleEntity> findByStatus(@Nonnull CourseScheduleStatus status);

}
