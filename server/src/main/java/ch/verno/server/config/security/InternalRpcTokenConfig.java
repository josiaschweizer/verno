package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.InternalRpcTokenCodec;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalRpcTokenConfig {

  @Nonnull
  @Bean
  public InternalRpcTokenCodec internalRpcTokenCodec(@Value("${verno.rpc.internal-secret}") @Nonnull final String secret) {
    return new InternalRpcTokenCodec(secret);
  }
}