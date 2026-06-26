package ch.verno.server.async;

import jakarta.annotation.Nonnull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class BackgroundExecutor {

  private static volatile BackgroundExecutor instance;

  private final ExecutorService executorService;

  private BackgroundExecutor() {
    this.executorService = Executors.newFixedThreadPool(
            5,
            new NamedThreadFactory("verno-bg-")
    );
  }

  @Nonnull
  public static BackgroundExecutor getInstance() {
    if (instance == null) {
      synchronized (BackgroundExecutor.class) {
        if (instance == null) {
          instance = new BackgroundExecutor();
        }
      }
    }
    return instance;
  }

  @Nonnull
  public ExecutorService getExecutorService() {
    return executorService;
  }

  public void shutdown() {
    executorService.shutdown();
  }

  private static class NamedThreadFactory implements ThreadFactory {

    private final String prefix;
    private final AtomicInteger counter = new AtomicInteger(1);

    private NamedThreadFactory(@Nonnull final String prefix) {
      this.prefix = prefix;
    }

    @Override
    public Thread newThread(@Nonnull Runnable r) {
      final var thread = new Thread(r);
      thread.setName(prefix + counter.getAndIncrement());
      thread.setDaemon(true);
      return thread;
    }
  }
}