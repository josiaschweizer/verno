package ch.verno.ui.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Component
public final class VernoI18nProvider implements I18NProvider {

  private static final String DEFAULT_BUNDLE_BASE = "i18n.messages";
  private static final List<Locale> PROVIDED =
          List.of(Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH);

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
      final var bundleBase = resolveBundleBase(key);
      final var bundle = ResourceBundle.getBundle(bundleBase, locale);
      final var pattern = bundle.getString(key);

      return (params == null || params.length == 0)
              ? pattern
              : MessageFormat.format(pattern, params);

    } catch (MissingResourceException e) {
      return fallback(key, locale, params);
    }
  }

  @Nonnull
  private String resolveBundleBase(@Nonnull final String key) {
    return I18nBundles.fromKey(key)
            .map(I18nBundles::getResourceBundleName)
            .orElse(DEFAULT_BUNDLE_BASE);
  }

  @Nonnull
  private String fallback(@Nonnull final String key,
                          @Nonnull final Locale locale,
                          final Object... params) {
    try {
      final var bundle = ResourceBundle.getBundle(DEFAULT_BUNDLE_BASE, locale);
      final var pattern = bundle.getString(key);
      return (params == null || params.length == 0)
              ? pattern
              : MessageFormat.format(pattern, params);
    } catch (MissingResourceException ignored) {
      return key;
    }
  }
}