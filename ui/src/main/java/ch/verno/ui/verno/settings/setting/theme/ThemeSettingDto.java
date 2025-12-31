package ch.verno.ui.verno.settings.setting.theme;

import ch.verno.common.db.dto.base.BaseDto;

public class ThemeSettingDto extends BaseDto {

  private boolean darkModeEnabled;

  public ThemeSettingDto() {
    this.darkModeEnabled = false;
  }

  public boolean isDarkModeEnabled() {
    return darkModeEnabled;
  }

  public void setDarkModeEnabled(final boolean darkModeEnabled) {
    this.darkModeEnabled = darkModeEnabled;
  }
}
