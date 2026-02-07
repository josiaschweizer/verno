package ch.verno.ui.verno.settings.setting.theme;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.db.service.IAppUserSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class UISetting extends VABaseSetting<UISettingDto> {

  public static final String TITLE_KEY = "setting.ui_settings";
  @Nonnull
  private final IAppUserSettingService appUserSettingService;
  @Nullable
  private AppUserDto currentUser;
  @Nullable
  private AppUserSettingDto currentSetting;

  public UISetting(@Nonnull final IAppUserService userService,
                   @Nonnull final IAppUserSettingService appUserSettingService) {
    super(TITLE_KEY, true);

    this.appUserSettingService = appUserSettingService;

    final var currentSecurityContextUser = getCurrentUser();
    if (currentSecurityContextUser == null) {
      throw new IllegalStateException("No authenticated user found.");
    }

    final var currentUser = userService.findByUserName(currentSecurityContextUser.getUsername());
    currentUser.ifPresent(appUserOptional -> this.currentUser = appUserOptional);

    loadCurrentSetting();
  }

  private void loadCurrentSetting() {
    if (currentUser == null || currentUser.getId() == null) {
      return;
    }

    try {
      currentSetting = appUserSettingService.getAppUserSettingByUserId(currentUser.getId());
      dto.setDarkModeEnabled("setting.dark".equals(currentSetting.getTheme()));
      dto.setLanguage(currentSetting.getLanguage());
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
            getTranslation("setting.theme"),
            getTranslation("setting.light"),
            getTranslation("setting.dark"),
            Optional.of(getTranslation("setting.toggle_between_light_and_dark_theme")),
            binder,
            UISettingDto::isDarkModeEnabled,
            UISettingDto::setDarkModeEnabled
    );
    final var languageSetting = settingEntryFactory.createComboBoxSetting(
            getTranslation("setting.language"),
            Optional.of(getTranslation("setting.select_your_preferred_language")),
            binder,
            UISettingDto::getLanguage,
            UISettingDto::setLanguage,
            List.of(Locale.GERMAN, Locale.ENGLISH, Locale.FRENCH),
            locale -> {
              final var label = locale.getDisplayLanguage(locale);
              return label.substring(0, 1).toUpperCase(locale) + label.substring(1);
            }
    );

    final var content = new VerticalLayout(themeToggle, languageSetting);
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

    final var theme = dto.isDarkModeEnabled() ? "setting.dark" : "setting.light";

    if (currentSetting != null) {
      currentSetting.setTheme(theme);
      currentSetting.setLanguage(dto.getLanguage());
      currentSetting = appUserSettingService.saveAppUserSetting(currentSetting);
    } else {
      final var newSetting = new AppUserSettingDto(
              currentUser.getId(),
              theme,
              dto.getLanguage());
      currentSetting = appUserSettingService.saveAppUserSetting(newSetting);
    }

    applyTheme(dto.isDarkModeEnabled());
    applyLanguage(dto.getLanguage());
  }

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

  public static void applyLanguage(@Nonnull final Locale locale) {
    final var ui = UI.getCurrent();
    if (ui == null) {
      return;
    }

    final var currentLocale = ui.getLocale();
    if (locale.equals(currentLocale)) {
      return;
    }

    ui.setLocale(locale);
    ui.getSession().setLocale(locale);
    ui.getPage().reload();
  }

  @Nonnull
  @Override
  protected Class<UISettingDto> getBeanType() {
    return UISettingDto.class;
  }

  @Nonnull
  @Override
  protected UISettingDto createNewBeanInstance() {
    return new UISettingDto(Locale.GERMAN);
  }
}
