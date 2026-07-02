package ch.verno.gateway.security;

import ch.verno.common.rpc.auth.pub.ResourceAccessTokenCodec;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResourceAccessTokenConfig {

  @Nonnull private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

  @Bean
  public ResourceAccessTokenCodec resourceAccessTokenCodec(@Value("${verno.api.resource-access-token-secret}") @Nonnull final String secret) { //TODO use config provider file
    return new ResourceAccessTokenCodec(secret, ACCESS_TOKEN_TTL);
  }
}