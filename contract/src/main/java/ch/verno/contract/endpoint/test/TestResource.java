package ch.verno.contract.endpoint.test;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface TestResource {

  @Nonnull
  String ping();

}