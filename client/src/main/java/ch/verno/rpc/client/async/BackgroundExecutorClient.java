package ch.verno.rpc.client.async;

import ch.verno.contract.endpoint.async.BackgroundExecutorResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.util.concurrent.Executor;

public class BackgroundExecutorClient {

  @Nonnull private final Lazy<BackgroundExecutorResource> backgroundExecutorResource;

  public BackgroundExecutorClient(@Nonnull final RpcFactory rpcFactory) {
    this.backgroundExecutorResource = Lazy.of(() -> rpcFactory.create(BackgroundExecutorResource.class));
  }

  @Nonnull
  public Executor getExecutorService() {
    return backgroundExecutorResource.get().getExecutorService();
  }

}
