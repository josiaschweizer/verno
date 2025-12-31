package ch.verno.ui.verno.settings.setting.theme;

import ch.verno.common.db.dto.AppUserDto;
import ch.verno.common.db.dto.AppUserSettingDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.server.service.AppUserService;
import ch.verno.server.service.AppUserSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
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

    final var darkModeToggle = new Checkbox("Dark");
    darkModeToggle.getElement().setAttribute("aria-label", "Toggle dark mode");
    darkModeToggle.addValueChangeListener(ev -> {
      if (ev.getValue()) {
        // set Lumo dark theme
        UI.getCurrent().getPage().executeJs("document.documentElement.setAttribute('theme','dark'); localStorage.setItem('v-theme','dark');");
      } else {
        UI.getCurrent().getPage().executeJs("document.documentElement.removeAttribute('theme'); localStorage.setItem('v-theme','light');");
      }
    });

    // Sync initial checkbox state from localStorage (if set to 'dark').
    UI.getCurrent().getPage().executeJs("return localStorage.getItem('v-theme');")
            .then(String.class, value -> {
              if ("dark".equals(value)) {
                darkModeToggle.setValue(true);
              }
            });
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

    final var content = new VerticalLayout();
    content.setPadding(false);

    return content;
  }

  @Nullable
  private User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      appUserSettingService.saveAppUserSetting(new AppUserSettingDto(null, dto.isDarkModeEnabled() ? "dark" : "light"));
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
