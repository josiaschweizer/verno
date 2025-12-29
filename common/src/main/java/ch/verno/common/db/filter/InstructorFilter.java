package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record InstructorFilter(@Nullable String searchText,
                               @Nullable Long genderId) {

  @Nonnull
  public static InstructorFilter ofSearchText(@Nullable final String searchText) {
    return new InstructorFilter(searchText, null);
  }

  @Nonnull
  public static InstructorFilter empty() {
    return new InstructorFilter(null, null);
  }
}
