package ch.verno.rpc.config;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import jakarta.annotation.Nonnull;

public class InternalRpcTokenModule extends AbstractModule {

  @Nonnull private final String secret;

  public InternalRpcTokenModule(@Nonnull final String secret) {
    this.secret = secret;
  }

  @Nonnull
  @Provides
  @Singleton
  public InternalRpcTokenCodec internalRpcTokenCodec() {
    return new InternalRpcTokenCodec(secret);
  }

}
