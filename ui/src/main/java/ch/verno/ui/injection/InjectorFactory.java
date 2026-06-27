package ch.verno.ui.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

public final class InjectorFactory {

  private InjectorFactory() {
  }

  @Nonnull
  public static Injector create(@Nonnull final I18NProvider i18n) {
    return Guice.createInjector(new UiGuiceModule(i18n));
  }
}