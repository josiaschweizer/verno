package ch.verno.ui.verno.settings.setting.theme;

import ch.verno.common.db.dto.AppUserDto;
import ch.verno.common.db.dto.AppUserSettingDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.server.service.AppUserSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public class ThemeSetting extends VABaseSetting<ThemeSettingDto> {

  @Nonnull
  private final IAppUserService appUserService;
  @Nonnull
  private final AppUserSettingService appUserSettingService;
  @Nullable
  private final AppUserDto currentUser;
  @Nullable
  private AppUserSettingDto currentSetting;

  public ThemeSetting(@Nonnull final IAppUserService appUserService,
                      @Nonnull final AppUserSettingService appUserSettingService) {
    super("Theme Settings", true);

    this.appUserService = appUserService;
    this.appUserSettingService = appUserSettingService;

    final var currentSecurityContextUser = getCurrentUser();
    if (currentSecurityContextUser == null) {
      throw new IllegalStateException("No authenticated user found.");
    }

    currentUser = appUserService.findByUserName(currentSecurityContextUser.getUsername());

    loadCurrentSetting();
  }

  private void loadCurrentSetting() {
    if (currentUser == null || currentUser.getId() == null) {
      return;
    }

    try {
      currentSetting = appUserSettingService.getAppUserSettingByUserId(currentUser.getId());
      dto.setDarkModeEnabled("dark".equals(currentSetting.getTheme()));
      binder.readBean(dto);
    } catch (Exception e) {
      dto.setDarkModeEnabled(false);
      binder.readBean(dto);
    }
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var themeToggle = settingEntryFactory.createToggleSetting(
            "Theme",
            "Light",
            "Dark",
            Optional.of("Toggle between light and dark theme."),
            binder,
            ThemeSettingDto::isDarkModeEnabled,
            ThemeSettingDto::setDarkModeEnabled
    );

    final var content = new VerticalLayout(themeToggle);
    content.setPadding(false);

    return content;
  }

  @Nullable
  private User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

  @Override
  protected void save() {
    if (!binder.writeBeanIfValid(dto) || currentUser == null || currentUser.getId() == null) {
      return;
    }

    final var theme = dto.isDarkModeEnabled() ? "dark" : "light";

    if (currentSetting != null) {
      currentSetting.setTheme(theme);
      appUserSettingService.saveAppUserSetting(currentSetting);
    } else {
      final var newSetting = new AppUserSettingDto(currentUser.getId(), theme);
      currentSetting = appUserSettingService.saveAppUserSetting(newSetting);
    }

    applyTheme(dto.isDarkModeEnabled());
  }

  /**
   * Applies the theme to the current UI.
   */
  public static void applyTheme(final boolean darkMode) {
    final var ui = UI.getCurrent();
    if (ui == null) {
      return;
    }

    if (darkMode) {
      ui.getPage().executeJs(
              "document.documentElement.setAttribute('theme','dark'); localStorage.setItem('v-theme','dark');"
      );
    } else {
      ui.getPage().executeJs(
              "document.documentElement.removeAttribute('theme'); localStorage.setItem('v-theme','light');"
      );
    }
  }

  @Nonnull
  @Override
  protected Binder<ThemeSettingDto> createBinder() {
    return new Binder<>(ThemeSettingDto.class);
  }

  @Nonnull
  @Override
  protected ThemeSettingDto createNewBeanInstance() {
    return new ThemeSettingDto();
  }
}
