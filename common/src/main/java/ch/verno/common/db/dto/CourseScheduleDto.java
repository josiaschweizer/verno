package ch.verno.common.db.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseScheduleDto(
    @Nullable Long id,
    @Nonnull Integer weekStart,
    @Nonnull Integer weekEnd
) {

  public static CourseScheduleDto empty() {
    return new CourseScheduleDto(
        0L,
        0,
        0
    );
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.weekStart() == 0
        && this.weekEnd() == 0;
  }

  public String displayName() {
    return "KW " + weekStart + "–" + weekEnd;
  }
}