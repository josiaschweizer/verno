package ch.verno.ui.verno.settings.panels.theme;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;

import java.util.Locale;

public class UISettingDto extends BaseDto {

  private boolean darkModeEnabled;

  @Nonnull
  private Locale language;

  public UISettingDto(@Nonnull final Locale language) {
    this.darkModeEnabled = false;
    this.language = language;
  }

  public boolean isDarkModeEnabled() {
    return darkModeEnabled;
  }

  public void setDarkModeEnabled(final boolean darkModeEnabled) {
    this.darkModeEnabled = darkModeEnabled;
  }

  @Nonnull
  public Locale getLanguage() {
    return language;
  }

  public void setLanguage(@Nonnull final Locale language) {
    this.language = language;
  }
}
