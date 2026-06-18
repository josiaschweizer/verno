package ch.verno.server.util;

import ch.verno.lib.Publ;
import ch.verno.lib.StringSanitizer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public class ServerStringUtil {

  @Nonnull
  public static String safeString(@Nullable final String input) {
    return Optional.ofNullable(StringSanitizer.clean(input)).orElse(Publ.EMPTY_STRING);
  }

}
