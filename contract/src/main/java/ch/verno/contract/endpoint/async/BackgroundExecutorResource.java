package ch.verno.contract.endpoint.async;

import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.concurrent.Executor;

@RpcEndpoint
public interface BackgroundExecutorResource {

  @Nonnull
  Executor getExecutorService();

}
