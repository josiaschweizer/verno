package ch.verno.server.config.rpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.annotation.Nonnull;

public class RpcServerModule extends AbstractModule {

  @Override
  protected void configure() {
    /*
     * RPC resources will be bound here later, for example:
     *
     * bind(CourseEndpoint.class).to(CourseResource.class);
     */
  }

  @Nonnull
  @Provides
  @Singleton
  @SuppressWarnings("unused")
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Nonnull
  @Provides
  @Singleton
  @SuppressWarnings("unused")
  RpcResourceRegistry rpcResourceRegistry(final com.google.inject.Injector injector) {
    final var registry = new RpcResourceRegistry();
    final var scanner = new RpcResourceScanner(injector);

    scanner.scanAndRegister(registry, "ch.verno.server.rpc.resource");

    return registry;
  }

  @Nonnull
  @Provides
  @Singleton
  @SuppressWarnings("unused")
  RpcDispatcher rpcDispatcher(final RpcResourceRegistry registry,
                              final ObjectMapper objectMapper) {
    return new RpcDispatcher(registry, objectMapper);
  }
}