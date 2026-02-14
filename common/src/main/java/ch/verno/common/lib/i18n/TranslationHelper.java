package ch.verno.common.lib.i18n;

import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class TranslationHelper {

  @Nonnull
  public static String getTranslation(@Nonnull final GlobalInterface globalInterface,
                                      @Nonnull final String key) {
    return getTranslation(globalInterface, key, (Object) null);
  }

  @Nonnull
  public static String getTranslation(@Nonnull final GlobalInterface globalInterface,
                                      @Nonnull final String key,
                                      @Nullable final Object... params) {
    return globalInterface.getI18NProvider().getTranslation(key, globalInterface.getLocale(), params);
  }

}
