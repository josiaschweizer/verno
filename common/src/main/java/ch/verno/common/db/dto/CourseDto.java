package ch.verno.common.db.dto;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.Set;

public record CourseDto(
    @Nullable Long id,
    @Nonnull String title,
    @Nullable Integer capacity,
    @Nonnull String location,
    @Nonnull CourseScheduleDto schedule,
    @Nonnull Set<DayOfWeek> weekdays,
    @Nullable Integer duration,
    @Nullable InstructorDto instructor
) {

  public static CourseDto empty() {
    return new CourseDto(
        0L,
        Publ.EMPTY_STRING,
        null,
        Publ.EMPTY_STRING,
        CourseScheduleDto.empty(),
        Set.of(),
        null,
        null
    );
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.title().isEmpty()
        && (this.capacity() == null || this.capacity() == 0)
        && this.location().isEmpty()
        && this.schedule().isEmpty()
        && this.weekdays().isEmpty()
        && (this.duration() == null || this.duration() == 0)
        && (this.instructor() == null || this.instructor().isEmpty());
  }

  public String displayName() {
    return title;
  }
}