package ch.verno.ui.i18n;

import jakarta.annotation.Nonnull;

public abstract class AbstractTranslationHelper {

  @Nonnull
  public String getTranslation(@Nonnull final String key) {
    return TranslationHelper.getTranslation(globalInterface, key);
  }

}
