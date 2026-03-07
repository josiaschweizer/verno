package ch.verno.common.lib.mail.placeholder;

import ch.verno.common.lib.mail.placeholder.context.MailContext;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public final class PlaceholderUtil {

  private PlaceholderUtil() {
  }

  @Nonnull
  public static <C extends MailContext> String replacePlaceholders(@Nonnull String content,
                                                                   @Nonnull C context,
                                                                   @Nonnull List<PlaceholderValue<C>> placeholderValues) {
    for (final var placeholder : placeholderValues) {
      final var replacement = placeholder.valueFunction().apply(context);
      content = content.replace(placeholder.placeholder().getValue(), nullToEmpty(replacement));
    }
    return content;
  }

  @Nonnull
  private static String nullToEmpty(@Nullable final String s) {
    return s == null ? Publ.EMPTY_STRING : s;
  }
}