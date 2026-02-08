package ch.verno.common.lib.i18n;

import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;

public class TranslationHelper {

  @Nonnull
  public static String getTranslation(@Nonnull final GlobalInterface globalInterface,
                                      @Nonnull final String key) {
    return globalInterface.getI18NProvider().getTranslation(key, globalInterface.getLocale());
  }

}
