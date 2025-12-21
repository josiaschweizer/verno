package ch.verno.server.persistence.mapper;

import ch.verno.domain.model.course.CourseUnit;
import ch.verno.domain.model.course.CourseUnitWeek;
import ch.verno.server.persistence.entity.CourseUnitEntity;
import ch.verno.server.persistence.entity.CourseUnitWeekEntity;
import jakarta.annotation.Nonnull;

import java.time.Instant;

public final class CourseUnitMapper {

  private CourseUnitMapper() {
  }

  public static @Nonnull CourseUnit toDomain(@Nonnull final CourseUnitEntity e) {
    final var weeks = e.getWeeks().stream()
        .map(w -> new CourseUnitWeek(w.getYear(), w.getCalendarWeek()))
        .toList();

    return new CourseUnit(
        e.getId(),
        e.getName(),
        weeks,
        e.getCreatedAt()
    );
  }

  public static @Nonnull CourseUnitEntity toNewEntity(@Nonnull final CourseUnit courseUnit) {
    final var entity = CourseUnitEntity.of(courseUnit.name(), courseUnit.createdAt());

    for (final var w : courseUnit.weeks()) {
      entity.addWeek(new CourseUnitWeekEntity(w.year(), w.calendarWeek()));
    }

    return entity;
  }

  public static void overwriteWeeks(@Nonnull final CourseUnitEntity entity,
                                    @Nonnull final CourseUnit courseUnit) {
    entity.clearWeeks();
    for (final var w : courseUnit.weeks()) {
      entity.addWeek(new CourseUnitWeekEntity(w.year(), w.calendarWeek()));
    }
  }

  public static @Nonnull Instant nowIfNull(@Nonnull final Instant value) {
    return value;
  }
}