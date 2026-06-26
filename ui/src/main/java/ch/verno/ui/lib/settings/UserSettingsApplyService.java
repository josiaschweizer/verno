package ch.verno.ui.lib.settings;

import ch.verno.lib.Lazy;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.verno.settings.panels.theme.ThemeSetting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsApplyService {

  @Nonnull private final Lazy<UserProperties> userProperties;

  @Inject
  public UserSettingsApplyService(@Nonnull final Injector injector) {
    this.userProperties = Lazy.of(() -> injector.getInstance(UserProperties.class));
  }

  public void applyCurrentUserSettings() {
    final var appUserOptional = userProperties.get().getOptionalCurrentAppUser();
    if (appUserOptional.isEmpty()) {
      return;
    }

    try {
      final var userSetting = userProperties.get().getCurrentAppUserSetting();
      final boolean isDarkMode = "setting.dark".equals(userSetting.getTheme());
      ThemeSetting.applyTheme(isDarkMode);
      ThemeSetting.applyLanguage(userSetting.getLanguage());
    } catch (Exception e) {
      applyThemeFromLocalStorage();
    }
  }

  private void applyThemeFromLocalStorage() {
    final var ui = UI.getCurrent();
    if (ui == null) {
      return;
    }

    ui.getPage()
            .executeJs("return localStorage.getItem('v-theme');")
            .then(String.class, theme -> {
              if ("setting.dark".equals(theme)) {
                ThemeSetting.applyTheme(true);
              }
            });
  }
}