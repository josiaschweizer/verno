package ch.verno.ui.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InjectorConfiguration {

  @Bean
  public Injector injector(@Nonnull final I18NProvider i18NProvider) {
    return Guice.createInjector(new UiGuiceModule(i18NProvider));
  }
}