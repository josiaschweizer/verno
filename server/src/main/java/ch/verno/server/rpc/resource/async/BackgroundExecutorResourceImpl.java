package ch.verno.server.rpc.resource.async;

import ch.verno.contract.endpoint.async.BackgroundExecutorResource;
import ch.verno.server.async.BackgroundExecutor;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

import java.util.concurrent.Executor;

public class BackgroundExecutorResourceImpl implements BackgroundExecutorResource {

  @Nonnull private final BackgroundExecutor backgroundExecutor;

  public BackgroundExecutorResourceImpl(@Nonnull final ServerBean serverBean) {
    this.backgroundExecutor = BackgroundExecutor.getInstance();
  }

  @Nonnull
  @Override
  public Executor getExecutorService() {
    return backgroundExecutor.getExecutorService();
  }

}
