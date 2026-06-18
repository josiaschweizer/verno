package ch.verno.server.rpc.resource.test;

import ch.verno.contract.rpc.RpcResource;
import ch.verno.contract.endpoint.test.TestEndpoint;
import jakarta.annotation.Nonnull;

@RpcResource(TestEndpoint.class)
public class TestResource implements TestEndpoint {

  @Nonnull
  @Override
  public String ping() {
    return "pong";
  }

}