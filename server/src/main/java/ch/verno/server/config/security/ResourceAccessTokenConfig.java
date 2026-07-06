package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.pub.ResourceAccessTokenCodec;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResourceAccessTokenConfig {

  @Nonnull private static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(5);

  @Bean
  public ResourceAccessTokenCodec resourceAccessTokenCodec(@Nonnull final ServerBean serverBean) {
    final var envBo = BoFactory.getInstance(serverBean).getEmptyConstructor(EnvironmentVariableBo.class);
    return new ResourceAccessTokenCodec(
            envBo.getEnv(VernoSecrets.API_RESOURCE_ACCESS_TOKEN),
            ACCESS_TOKEN_TTL
    );
  }
}