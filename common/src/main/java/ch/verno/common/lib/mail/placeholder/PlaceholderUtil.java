package ch.verno.common.lib.mail.placeholder;

import jakarta.annotation.Nonnull;

import java.util.List;

public final class PlaceholderUtil {

  private PlaceholderUtil() {
  }

  @Nonnull
  public static <T> String replacePlaceholders(@Nonnull String content,
                                               @Nonnull T source,
                                               @Nonnull List<PlaceholderValue<T>> placeholderValues) {
    for (final var pv : placeholderValues) {
      final var replacement = pv.valueFunction().apply(source);
      content = content.replace(pv.placeholder().getValue(), nullToEmpty(replacement));
    }
    return content;
  }

  @Nonnull
  private static String nullToEmpty(final String s) {
    return s == null ? "" : s;
  }
}