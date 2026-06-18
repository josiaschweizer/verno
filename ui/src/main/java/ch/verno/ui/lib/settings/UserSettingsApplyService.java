package ch.verno.ui.lib.settings;

import ch.verno.common.server.service.intern.user.IAppUserSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.Publ;
import ch.verno.ui.verno.settings.panels.theme.ThemeSetting;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSettingsApplyService {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IAppUserSettingService appUserSettingService;

  public UserSettingsApplyService(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.appUserSettingService = globalInterface.getService(IAppUserSettingService.class);
  }

  public void applyCurrentUserSettings() {
    final var appUserOptional = globalInterface.getUserProperties().getOptionalCurrentUser();
    if (appUserOptional.isEmpty()) {
      return;
    }

    final var currentUser = appUserOptional.get();

    try {
      final var currentUserId = Optional.ofNullable(currentUser.getId()).orElse(Publ.ZERO_LONG);
      final var userSetting = appUserSettingService.getAppUserSettingByUserId(currentUserId);

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