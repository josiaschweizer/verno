package ch.verno.contract.endpoint.async;

import jakarta.annotation.Nonnull;

import java.util.concurrent.Executor;

public interface BackgroundExecutorResource {

  @Nonnull
  Executor getExecutorService();

}
