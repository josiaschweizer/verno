package ch.verno.contract.dto.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseLevelFilter(@Nullable String searchText,
                                @Nullable Long minSortingOrder,
                                @Nullable Long maxSortingOrder) {

  @Nonnull
  public static CourseLevelFilter ofSearchText(@Nullable final String searchText) {
    return new CourseLevelFilter(searchText, null, null);
  }

  @Nonnull
  public static CourseLevelFilter empty() {
    return new CourseLevelFilter(null, null, null);
  }

}
