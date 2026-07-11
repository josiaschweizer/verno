package ch.verno.rpc.properties.env;

import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.lib.VernoSecrets;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class EnvProperties {

  @Nonnull private final EnvResource envResource;

  @Inject
  public EnvProperties(@Nonnull final RpcFactory rpcFactory) {
    this.envResource = rpcFactory.create(EnvResource.class);
  }

  @Nonnull
  public String getApiPassword() {
    return envResource.getEnv(VernoSecrets.API_PASSWORD);
  }

  @Nonnull
  public String getEncodedApiPassword() {
    final var apiPassword = getApiPassword();
    return envResource.encodeString(apiPassword);
  }

}
