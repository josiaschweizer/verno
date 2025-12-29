package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseFilter(@Nullable String searchText,
                           @Nullable Long instructorId,
                           @Nullable Long courseScheduleId,
                           @Nullable Long courseLevelId) {

  @Nonnull
  public static CourseFilter fromSearchText(@Nullable final String searchText) {
    return new CourseFilter(searchText, null, null, null);
  }

  public static CourseFilter empty() {
    return new CourseFilter(null, null, null, null);
  }

}
