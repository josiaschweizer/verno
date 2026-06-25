package ch.verno.contract.endpoint.properties.application;

import ch.verno.common.lib.application.RunMode;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface ApplicationResource {

  @Nonnull
  RunMode getRunMode();

}
