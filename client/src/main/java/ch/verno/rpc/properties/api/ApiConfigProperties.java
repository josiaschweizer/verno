package ch.verno.rpc.properties.api;

import ch.verno.contract.endpoint.properties.api.ApiConfigResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ApiConfigProperties {

  @Nonnull private final Lazy<ApiConfigResource> apiConfigResource;

  @Inject
  public ApiConfigProperties(@Nonnull final RpcFactory rpcFactory) {
    this.apiConfigResource = Lazy.of(() -> rpcFactory.create(ApiConfigResource.class));
  }

  @Nonnull
  public String getBaseUrl() {
    return apiConfigResource.get().getBaseUrl();
  }

}
