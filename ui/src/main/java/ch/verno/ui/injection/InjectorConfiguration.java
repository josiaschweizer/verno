package ch.verno.ui.injection;

import ch.verno.rpc.config.InternalRpcTokenModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InjectorConfiguration {

  @Bean
  public Injector injector(@Nonnull final I18NProvider i18NProvider,
                           @Value("${verno.rpc.internal-secret}")@Nonnull final String rpcInternalSecret) {
    //TODO move internal secret into env file and then use the InternalRpcTokenModule
    return Guice.createInjector(
            new GuiceModule(i18NProvider),
            new InternalRpcTokenModule(rpcInternalSecret)
    );
  }
}