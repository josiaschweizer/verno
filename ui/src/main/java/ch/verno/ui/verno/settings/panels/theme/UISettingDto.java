package ch.verno.ui.verno.settings.panels.theme;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.lib.language.Language;
import jakarta.annotation.Nonnull;

import java.util.Locale;

public class UISettingDto extends BaseDto {

  private boolean darkModeEnabled;

  @Nonnull
  private Locale locale;

  public UISettingDto(@Nonnull final Locale locale) {
    this.darkModeEnabled = false;
    this.locale = locale;
  }

  public boolean isDarkModeEnabled() {
    return darkModeEnabled;
  }

  public void setDarkModeEnabled(final boolean darkModeEnabled) {
    this.darkModeEnabled = darkModeEnabled;
  }

  @Nonnull
  public Locale getLocale() {
    return locale;
  }

  @Nonnull
  public Language getLanguage() {
    return Language.of(locale.toLanguageTag());
  }

  public void setLocale(@Nonnull final Locale locale) {
    this.locale = locale;
  }
}
