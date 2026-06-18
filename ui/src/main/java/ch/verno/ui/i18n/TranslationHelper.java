package ch.verno.ui.i18n;

import com.google.inject.Inject;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Locale;

public class TranslationHelper {

  @Nonnull private final I18NProvider i18NProvider;

  @Inject
  public TranslationHelper(@Nonnull final I18NProvider i18NProvider) {
    this.i18NProvider = i18NProvider;
  }

  @Nonnull
  public String getTranslation(@Nonnull final String key) {
    return getTranslation(key, (Object) null);
  }

  @Nonnull
  public String getTranslation(@Nonnull final String key,
                               @Nullable final Object... params) {
    return i18NProvider.getTranslation(key, getLocale(), params);
  }

  @Nonnull
  private Locale getLocale() {
    final var ui = UI.getCurrent();
    return ui != null ? ui.getLocale() : Locale.getDefault();
  }
}