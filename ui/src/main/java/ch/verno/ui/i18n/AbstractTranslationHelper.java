package ch.verno.ui.i18n;

import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public abstract class AbstractTranslationHelper {

  @Nonnull protected final Injector injector;

  public AbstractTranslationHelper(@Nonnull final Injector injector){
    this.injector = injector;
  }

  @Nonnull
  public String getTranslation(@Nonnull final String key) {
    return injector.getInstance(TranslationHelper.class).getTranslation(key);
  }

}
