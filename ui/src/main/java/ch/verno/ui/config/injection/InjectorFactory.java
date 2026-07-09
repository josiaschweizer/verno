package ch.verno.ui.config.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;

public final class InjectorFactory {

  private InjectorFactory() {
  }

  @Nonnull
  public static Injector create(@Nonnull final String rpcUrl,
                                @Nonnull final I18NProvider i18n) {
    return Guice.createInjector(new GuiceModule(rpcUrl, i18n));
  }
}