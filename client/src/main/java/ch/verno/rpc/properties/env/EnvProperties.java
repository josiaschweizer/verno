package ch.verno.rpc.properties.env;

import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.contract.endpoint.properties.env.EnvResource;
import ch.verno.lib.Lazy;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class EnvProperties {

  @Nonnull private final Lazy<EnvResource> envResource;

  @Inject
  public EnvProperties(@Nonnull final RpcFactory rpcFactory) {
    this.envResource = Lazy.of(() -> rpcFactory.create(EnvResource.class));
  }

}
