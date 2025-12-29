package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseScheduleFilter(@Nullable String searchText,
                                   @Nullable Integer week) {

  @Nonnull
  public static CourseScheduleFilter fromSearchText(@Nullable final String searchText) {
    return new CourseScheduleFilter(searchText, null);
  }

  @Nonnull
  public static CourseScheduleFilter empty() {
    return new CourseScheduleFilter(null, null);
  }
}
