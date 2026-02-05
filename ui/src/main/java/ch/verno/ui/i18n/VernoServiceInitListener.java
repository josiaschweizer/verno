package ch.verno.ui.i18n;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.db.service.IAppUserSettingService;
import ch.verno.ui.base.error.GlobalErrorHandler;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public final class VernoServiceInitListener implements VaadinServiceInitListener {

  private static final Logger LOG = LoggerFactory.getLogger(VernoServiceInitListener.class);

  private final ApplicationContext applicationContext;

  // Use ApplicationContext so we don't require the IAppUserService/IAppUserSettingService
  // beans to be present at construction time (avoids hard dependency on server module).
  public VernoServiceInitListener(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void serviceInit(@Nonnull ServiceInitEvent event) {
    event.getSource().addSessionInitListener(sessionEvent -> {
      sessionEvent.getSession().setErrorHandler(new GlobalErrorHandler());
    });

    event.getSource().addUIInitListener(uiEvent -> {
      final var ui = uiEvent.getUI();

      final var locale = loadLocaleFromDatabase();
      ui.setLocale(locale);

      final var session = ui.getSession();
      if (session != null) {
        session.setLocale(locale);
      }
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
    LOG.debug("VernoServiceInitListener.getCurrentUser: authentication={} thread={}", authentication, Thread.currentThread().getName());
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }
}