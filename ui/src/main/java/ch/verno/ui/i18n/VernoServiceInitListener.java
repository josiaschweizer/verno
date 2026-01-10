package ch.verno.ui.i18n;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.db.service.IAppUserSettingService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Locale;

@Component
public final class VernoServiceInitListener implements VaadinServiceInitListener {

  private final ApplicationContext applicationContext;

  // Use ApplicationContext so we don't require the IAppUserService/IAppUserSettingService
  // beans to be present at construction time (avoids hard dependency on server module).
  public VernoServiceInitListener(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
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
      final var appUserService = applicationContext.getBeanProvider(IAppUserService.class).getIfAvailable();
      final var appUserSettingService = applicationContext.getBeanProvider(IAppUserSettingService.class).getIfAvailable();

      // If either service implementation is not available, fallback to default locale
      if (appUserService == null || appUserSettingService == null) {
        return Locale.GERMAN;
      }

      final var appUserOptional = appUserService.findByUserName(currentUser.getUsername());
      if (appUserOptional.isEmpty() || appUserOptional.get().getId() == null) {
        return Locale.GERMAN;
      }

      final var appUser = appUserOptional.get();

      final var userSetting = appUserSettingService.getAppUserSettingByUserId(appUser.getId());
      return userSetting.getLanguage();
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