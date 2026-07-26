package ch.verno.server.repository.course;

import ch.verno.common.type.course.courseschedule.status.CourseScheduleStatus;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.db.jpa.course.SpringDataCourseScheduleJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseScheduleRepository extends AbstractEntityRepository<
        CourseScheduleEntity,
        Long,
        SpringDataCourseScheduleJpaRepository> {

  public CourseScheduleRepository(@Nonnull final SpringDataCourseScheduleJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public List<CourseScheduleEntity> findByWeek(@Nonnull final String week) {
    return getRepository().findByWeeksContains(week);
  }

  @Nonnull
  public List<CourseScheduleEntity> findByStatus(@Nonnull final CourseScheduleStatus status) {
    return getRepository().findByStatus(status);
  }
}
