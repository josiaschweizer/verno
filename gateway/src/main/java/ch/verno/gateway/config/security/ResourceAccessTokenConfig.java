package ch.verno.gateway.config.security;

import ch.verno.common.rpc.auth.pub.ResourceAccessTokenCodec;
import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.lib.VernoSecrets;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResourceAccessTokenConfig {

  @Nonnull private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

  @Bean
  public ResourceAccessTokenCodec resourceAccessTokenCodec(@Nonnull final RpcFactory rpcFactory) {
    final var envResource = rpcFactory.create(EnvResource.class);
    return new ResourceAccessTokenCodec(
            envResource.getEnv(VernoSecrets.API_RESOURCE_ACCESS_TOKEN),
            ACCESS_TOKEN_TTL
    );
  }
}