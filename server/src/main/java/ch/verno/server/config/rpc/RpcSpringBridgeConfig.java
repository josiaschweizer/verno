package ch.verno.server.config.rpc;

import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcSpringBridgeConfig {

  @Bean
  @Nonnull
  public RpcDispatcher rpcDispatcher(@Nonnull final Injector injector) {
    return injector.getInstance(RpcDispatcher.class);
  }
}