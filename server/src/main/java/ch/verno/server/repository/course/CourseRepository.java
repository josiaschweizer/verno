package ch.verno.server.repository.course;

import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.jpa.course.SpringDataCourseJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseRepository extends AbstractEntityRepository<
        CourseEntity,
        Long,
        SpringDataCourseJpaRepository> {

  public CourseRepository(@Nonnull final SpringDataCourseJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public List<CourseEntity> findByCourseScheduleId(@Nonnull final Long courseScheduleId) {
    return getRepository().findByCourseSchedule_Id(courseScheduleId);
  }

  public boolean existsByInstructorId(@Nonnull final Long instructorId) {
    return getRepository().existsByInstructor_Id(instructorId);
  }

}
