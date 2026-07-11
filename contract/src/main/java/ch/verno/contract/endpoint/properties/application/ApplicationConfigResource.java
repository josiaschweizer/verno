package ch.verno.contract.endpoint.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface ApplicationConfigResource {

  @Nonnull
  RunMode getRunMode();

  @Nonnull
  String getRpcUrl();

  @Nonnull
  String getApiUrl();

  @Nonnull
  String getApiUsername();

}
