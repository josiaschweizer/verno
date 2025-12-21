package ch.verno.server.persistence.mapper;

import ch.verno.domain.model.course.Course;
import ch.verno.server.persistence.entity.CourseEntity;
import jakarta.annotation.Nonnull;

public final class CourseMapper {

  private CourseMapper() {
  }

  public static @Nonnull Course toDomain(@Nonnull final CourseEntity entity) {
    final var unit = entity.getCourseUnit();

    return new Course(
        entity.getId(),
        entity.getTitle(),
        entity.getCapacity(),
        entity.getLocation(),
        entity.getDurationMinutes(),
        entity.getCourseLevelId(),
        entity.getInstructorId(),
        unit != null ? unit.getId() : null,
        entity.getCreatedAt()
    );
  }
}