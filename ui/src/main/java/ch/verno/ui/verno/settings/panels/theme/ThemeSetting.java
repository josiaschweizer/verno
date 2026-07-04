package ch.verno.ui.verno.settings.panels.theme;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.lib.language.Language;
import ch.verno.rpc.client.setting.AppUserSettingClient;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.common.lib.theme.ThemeConstants;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ThemeSetting extends VABaseSetting<UISettingDto> {

  @NonNls public static final String TITLE_KEY = "setting.ui_settings";
  @NonNls public static final String TOGGLE_DARK_MODE_JS = "document.documentElement.setAttribute('theme','dark'); localStorage.setItem('v-theme','dark');";
  @NonNls public static final String TOGGLE_LIGHT_MODE_JS = "document.documentElement.removeAttribute('theme'); localStorage.setItem('v-theme','light');";

  @Nonnull private final Lazy<AppUserSettingClient> appUserSettingClient;
  @Nonnull private final AppUserSettingDto currentAppUserSetting;

  @Inject
  public ThemeSetting(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);

    this.appUserSettingClient = Lazy.of(() -> injector.getInstance(AppUserSettingClient.class));
    this.currentAppUserSetting = injector.getInstance(AppUserClient.class).getCurrentOrEmptyAppUserSetting();
  }

  @Override
  protected void loadDto() {
    dto.setDarkModeEnabled(ThemeConstants.SETTING_DARK.equals(currentAppUserSetting.getTheme()));
    dto.setLocale(currentAppUserSetting.getLanguage().getLocale());
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
    if (!binder.writeBeanIfValid(dto)) {
      return;
    }

    currentAppUserSetting.setTheme(dto.isDarkModeEnabled() ? ThemeConstants.SETTING_DARK : ThemeConstants.SETTING_LIGHT);
    currentAppUserSetting.setLanguage(dto.getLanguage());
    appUserSettingClient.get().saveAppUserSetting(currentAppUserSetting);

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
