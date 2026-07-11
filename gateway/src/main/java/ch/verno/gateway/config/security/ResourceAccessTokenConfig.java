package ch.verno.gateway.config.security;

import ch.verno.common.rpc.auth.pub.ResourceAccessTokenCodec;
import ch.verno.lib.properties.ApplicationPropertiesConstants;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResourceAccessTokenConfig {

  @Nonnull private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

  @Bean
  public ResourceAccessTokenCodec resourceAccessTokenCodec(
          @Value(ApplicationPropertiesConstants.API_RESOURCE_ACCESS_TOKEN) @Nonnull final String secret) {
    return new ResourceAccessTokenCodec(secret, ACCESS_TOKEN_TTL);
  }
}