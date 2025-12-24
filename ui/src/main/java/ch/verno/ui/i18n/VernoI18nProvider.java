package ch.verno.ui.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class VernoI18nProvider implements I18NProvider {

  private static final String BUNDLE_BASE = "i18n.messages";
  private static final List<Locale> PROVIDED = List.of(Locale.GERMAN, Locale.ENGLISH);

  @Nonnull
  @Override
  public List<Locale> getProvidedLocales() {
    return PROVIDED;
  }

  @Nonnull
  @Override
  public String getTranslation(@Nonnull final String key,
                               @Nonnull final Locale locale,
                               final Object... params) {
    try {
      final var bundle = ResourceBundle.getBundle(BUNDLE_BASE, locale);
      final var pattern = bundle.getString(key);
      return (params == null || params.length == 0)
          ? pattern
          : MessageFormat.format(pattern, params);
    } catch (MissingResourceException e) {
      return key;
    }
  }
}