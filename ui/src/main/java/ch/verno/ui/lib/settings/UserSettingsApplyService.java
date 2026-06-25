package ch.verno.ui.lib.settings;

import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.setting.AppUserSettingClient;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.verno.settings.panels.theme.ThemeSetting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSettingsApplyService {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<AppUserSettingClient> appUserSettingClient;

  @Inject
  public UserSettingsApplyService(@Nonnull final Injector injector) {
    this.injector = injector;
    this.appUserSettingClient = Lazy.of(() -> injector.getInstance(AppUserSettingClient.class));
  }

  public void applyCurrentUserSettings() {
    final var appUserOptional = injector.getInstance(UserProperties.class).getOptionalCurrentAppUser();
    if (appUserOptional.isEmpty()) {
      return;
    }

    final var currentUser = appUserOptional.get();

    try {
      final var currentUserId = Optional.ofNullable(currentUser.getId()).orElse(Publ.ZERO_LONG);
      final var userSetting = appUserSettingClient.get().getAppUserSettingByUserId(currentUserId);

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