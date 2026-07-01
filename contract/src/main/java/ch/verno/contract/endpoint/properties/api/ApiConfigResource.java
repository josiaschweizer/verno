package ch.verno.contract.endpoint.properties.api;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface ApiConfigResource {

  @Nonnull
  String getBaseUrl();

  @Nonnull
  String getApiUsername();

  @Nonnull
  String getApiPassword();
}
