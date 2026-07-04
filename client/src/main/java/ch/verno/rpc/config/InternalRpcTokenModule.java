package ch.verno.rpc.config;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.annotation.Nonnull;

public class InternalRpcTokenModule extends AbstractModule {

  @Nonnull
  @Provides
  @Singleton
  public InternalRpcTokenCodec internalRpcTokenCodec() {
    final var secret = System.getenv("VERNO_RPC_INTERNAL_SECRET");
    if (secret == null || secret.isBlank()) {
      throw new IllegalStateException("VERNO_RPC_INTERNAL_SECRET is not set");
    }

    return new InternalRpcTokenCodec(secret);
  }
}