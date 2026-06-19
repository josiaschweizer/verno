package ch.verno.server.rpc.resource.test;

import ch.verno.contract.rpc.RpcResource;
import ch.verno.contract.endpoint.test.TestResource;
import jakarta.annotation.Nonnull;

@RpcResource(TestResource.class)
public class TestResourceImpl implements TestResource {

  @Nonnull
  @Override
  public String ping() {
    return "pong";
  }

}