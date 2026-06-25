package ch.verno.ui.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class InjectorFactory {

  private InjectorFactory() {
  }

  public static Injector create() {
    return new Injector(
            Guice.createInjector(new UiGuiceModule())
    );
  }
}