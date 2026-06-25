package ch.verno.ui.config.i18n;

import ch.verno.lib.lib.language.Language;
import ch.verno.lib.lib.language.LanguageUtil;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.base.error.GlobalErrorHandler;
import ch.verno.ui.injection.InjectorFactory;
import com.google.inject.Injector;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class VernoServiceInitListener implements VaadinServiceInitListener {

  private static final Logger LOG = LoggerFactory.getLogger(VernoServiceInitListener.class);

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    final var injector = InjectorFactory.create();

    event.getSource().addSessionInitListener(sessionEvent -> {
      final var errorHandler = injector.getInstance(GlobalErrorHandler.class);
      sessionEvent.getSession().setErrorHandler(errorHandler);
    });

    event.getSource().addUIInitListener(uiEvent -> {
      final var ui = uiEvent.getUI();

      final var language = getUserLanguage(injector);
      final var locale = Locale.forLanguageTag(language.getCode());
      ui.setLocale(locale);
      ui.getSession().setLocale(locale);
    });
  }

  @Nonnull
  private Language getUserLanguage(@Nonnull final Injector injector) {
    // first check if there is a user logged in
    final var userProperties = injector.getInstance(UserProperties.class);
    final var currentUser = userProperties.getOptionalCurrentAppUser();
    if (currentUser.isPresent()) {
      return LanguageUtil.getDefaultLanguage();
    }

    return userProperties.getCurrentUserLanguage();
  }
}