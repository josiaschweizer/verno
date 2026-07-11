package ch.verno.ui.config.injection;

import ch.verno.lib.properties.ApplicationPropertiesConstants;
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
                           @Value(ApplicationPropertiesConstants.VERNO_RPC_URL) @Nonnull final String rpcUrl,
                           @Value(ApplicationPropertiesConstants.VERNO_RPC_SECRET) @Nonnull final String rpcInternalSecret) {
    return Guice.createInjector(
            new GuiceModule(rpcUrl, i18NProvider),
            new InternalRpcTokenModule(rpcInternalSecret)
    );
  }
}