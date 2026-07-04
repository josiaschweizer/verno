package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.server.application.properties.RpcConfigProvider;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalRpcTokenConfig {

  @Nonnull
  @Bean
  public InternalRpcTokenCodec internalRpcTokenCodec(@Nonnull final RpcConfigProvider rpcConfigProvider) {
    return new InternalRpcTokenCodec(rpcConfigProvider.getInternalSecret());
  }
}