package ch.verno.ui.verno.settings.panels.theme;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.common.server.service.intern.user.IAppUserService;
import ch.verno.common.server.service.intern.user.IAppUserSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.lib.language.Language;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.theme.ThemeConstants;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ThemeSetting extends VABaseSetting<UISettingDto> {

  @NonNls public static final String TITLE_KEY = "setting.ui_settings";
  @NonNls public static final String TOGGLE_DARK_MODE_JS = "document.documentElement.setAttribute('theme','dark'); localStorage.setItem('v-theme','dark');";
  @NonNls public static final String TOGGLE_LIGHT_MODE_JS = "document.documentElement.removeAttribute('theme'); localStorage.setItem('v-theme','light');";

  @Nonnull private final IAppUserSettingService appUserSettingService;

  @Nullable private AppUserDto currentUser;
  @Nullable private AppUserSettingDto currentSetting;

  @Inject
  public ThemeSetting(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);

    this.appUserSettingService = globalInterface.getService(IAppUserSettingService.class);

    final var currentSecurityContextUser = globalInterface.getUserProperties().getCurrentSpringUser();
    if (currentSecurityContextUser == null) {
      throw new IllegalStateException("No authenticated user found.");
    }

    final var userService = globalInterface.getService(IAppUserService.class);
    final var currentUser = userService.findByUserName(currentSecurityContextUser.getUsername());
    currentUser.ifPresent(appUserOptional -> this.currentUser = appUserOptional);

    loadCurrentSetting();
  }

  private void loadCurrentSetting() {
    if (currentUser == null || currentUser.getId() == null) {
      return;
    }

    try {
      this.currentSetting = appUserSettingService.getAppUserSettingByUserId(currentUser.getId());
      dto.setDarkModeEnabled(ThemeConstants.SETTING_DARK.equals(currentSetting.getTheme()));
      dto.setLocale(currentSetting.getLanguage().getLocale());
    } catch (Exception e) {
      dto.setDarkModeEnabled(false);
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
            UISettingDto::getLocale,
            UISettingDto::setLocale,
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

  @Override
  protected void save() {
    if (!binder.writeBeanIfValid(dto) || currentUser == null || currentUser.getId() == null) {
      return;
    }

    final var theme = dto.isDarkModeEnabled() ?
            ThemeConstants.SETTING_DARK :
            ThemeConstants.SETTING_LIGHT;

    if (currentSetting != null) {
      currentSetting.setTheme(theme);
      currentSetting.setLanguage(dto.getLanguage());
      currentSetting = appUserSettingService.saveAppUserSetting(currentSetting);
    } else {
      final var newSetting = new AppUserSettingDto(
              currentUser.getId(),
              theme,
              dto.getLanguage()
      );
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
      ui.getPage().executeJs(TOGGLE_DARK_MODE_JS);
    } else {
      ui.getPage().executeJs(TOGGLE_LIGHT_MODE_JS);
    }
  }

  public static void applyLanguage(@Nonnull final Language language) {
    final var ui = UI.getCurrent();
    if (ui == null) {
      return;
    }

    final var locale = Locale.forLanguageTag(language.getCode());
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
