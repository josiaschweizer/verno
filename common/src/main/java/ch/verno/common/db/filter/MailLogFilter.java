package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MailLogFilter(@Nullable String searchText) {

  @Nonnull
  public static MailLogFilter ofSearchText(@Nullable final String searchText) {
    return new MailLogFilter(searchText);
  }

  @Nonnull
  public static MailLogFilter empty() {
    return new MailLogFilter(null);
  }
}
