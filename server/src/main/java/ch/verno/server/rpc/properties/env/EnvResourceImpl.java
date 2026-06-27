package ch.verno.server.rpc.properties.env;

import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(EnvResource.class)
public class EnvResourceImpl implements EnvResource {

  @Nonnull private final Lazy<EnvironmentVariableBo> environmentVariableBo;

  public EnvResourceImpl(@Nonnull final ServerBean bean) {
    this.environmentVariableBo = Lazy.of(() -> bean.get(BoFactory.class).get(EnvironmentVariableBo.class));
  }

  @Nonnull
  @Override
  public String getEnv(@Nonnull String key) {
    return environmentVariableBo.get().getEnv(key);
  }

  @Nonnull
  @Override
  public String getEnv(@Nonnull String key, @Nonnull String defaultValue) {
    return environmentVariableBo.get().getEnvOrDefault(key, defaultValue);
  }

  @Nonnull
  @Override
  public String getEnvNullable(@Nonnull String key) {
    return environmentVariableBo.get().getEnvNullable(key);
  }

}
