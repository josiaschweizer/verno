package ch.verno.lib;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class StringSanitizer {

  private StringSanitizer() {
  }

  @Nullable
  public static String clean(@Nullable final String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }

    return cleanNullSave(value);
  }

  @Nonnull
  public static String cleanNullSave(@Nonnull final String value) {
    return value
            .replace(Publ.BOM_ZWNBSP, Publ.EMPTY_STRING)
            .replace(Publ.ZERO_WIDTH_SPACE, Publ.EMPTY_STRING)
            .replace(Publ.ZERO_WIDTH_NON_JOINER, Publ.EMPTY_STRING)
            .replace(Publ.ZERO_WIDTH_JOINER, Publ.EMPTY_STRING)
            .strip();
  }

}
