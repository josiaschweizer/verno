package ch.verno.ui.i18n;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.server.service.AppUserSettingService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public final class VernoServiceInitListener implements VaadinServiceInitListener {

  @Nonnull
  private final IAppUserService appUserService;

  @Nonnull
  private final AppUserSettingService appUserSettingService;

  public VernoServiceInitListener(@Nonnull final IAppUserService appUserService,
                                  @Nonnull final AppUserSettingService appUserSettingService) {
    this.appUserService = appUserService;
    this.appUserSettingService = appUserSettingService;
  }

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    event.getSource().addUIInitListener(uiEvent -> {
      final UI ui = uiEvent.getUI();
      final Locale locale = loadLocaleFromDatabase();
      ui.setLocale(locale);
      VaadinSession.getCurrent().setLocale(locale);
    });
  }

  @Nonnull
  private Locale loadLocaleFromDatabase() {
    final var currentUser = getCurrentUser();
    if (currentUser == null) {
      return Locale.GERMAN;
    }

    try {
      final var appUser = appUserService.findByUserName(currentUser.getUsername());
      if (appUser == null || appUser.getId() == null) {
        return Locale.GERMAN;
      }

      final var userSetting = appUserSettingService.getAppUserSettingByUserId(appUser.getId());
      if (userSetting != null && userSetting.getLanguage() != null) {
        return userSetting.getLanguage();
      }
    } catch (Exception ignored) {
      // Fallback to default
    }

    return Locale.GERMAN;
  }

  private User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }
}