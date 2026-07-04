package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InternalRpcTokenConfig {

  @Bean
  @Nonnull
  public InternalRpcTokenCodec internalRpcTokenCodec(@Nonnull final ServerBean serverBean) {
    final var environmentVariableBo = BoFactory.getInstance(serverBean).getEmptyConstructor(EnvironmentVariableBo.class);
    return new InternalRpcTokenCodec(environmentVariableBo.getEnv(VernoSecrets.RPC_INTERNAL_SECRET));
  }
}