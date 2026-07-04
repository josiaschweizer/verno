package ch.verno.ui.config.i18n;

import ch.verno.lib.lib.language.Language;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.ui.base.error.GlobalErrorHandler;
import ch.verno.ui.injection.InjectorFactory;
import com.google.inject.Injector;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class VernoServiceInitListener implements VaadinServiceInitListener {

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    final var i18nProvider = event.getSource()
            .getInstantiator()
            .getOrCreate(I18NProvider.class);

    final var injector = InjectorFactory.create(i18nProvider);

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
    final var appUserClient = injector.getInstance(AppUserClient.class);
    return appUserClient.getCurrentOrDefaultUserLanguage();
  }
}