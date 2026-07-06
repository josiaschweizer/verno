package ch.verno.rpc.properties.api;

import ch.verno.contract.endpoint.properties.application.ApplicationConfigResource;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ApiConfigProperties {

  @Nonnull private final ApplicationConfigResource applicationConfigResource;

  @Inject
  public ApiConfigProperties(@Nonnull final RpcFactory rpcFactory) {
    this.applicationConfigResource = rpcFactory.create(ApplicationConfigResource.class);
  }

  @Nonnull
  public String getRpcUrl() {
    return applicationConfigResource.getRpcUrl();
  }

}
