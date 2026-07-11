package ch.verno.contract.mail.placeholder;

import ch.verno.contract.mail.placeholder.base.MailContext;
import ch.verno.contract.mail.placeholder.base.Placeholder;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public final class PlaceholderUtil {

  private PlaceholderUtil() {
  }

  @Nonnull
  public static <C extends MailContext, P extends Placeholder> String replacePlaceholders(@Nonnull final PlaceholderResolver<C, P> placeholderResolver,
                                                                                          @Nonnull String content,
                                                                                          @Nonnull final C context,
                                                                                          @Nonnull final List<P> placeholders) {
    for (final var placeholder : placeholders) {
      final var valueFunction = placeholderResolver.getValueFunctionForPlaceholder(placeholder);
      final var replacement = valueFunction.apply(context);
      content = content.replace(placeholder.getValue(), nullToEmpty(replacement));
    }
    return content;
  }

  @Nonnull
  private static String nullToEmpty(@Nullable final String s) {
    return s == null ? Publ.EMPTY_STRING : s;
  }
}