package ch.verno.lib.exception.rpc.auth.internal;

import jakarta.annotation.Nullable;

public class InternalRpcTokenException extends RuntimeException {

  public InternalRpcTokenException(@Nullable final String message) {
    super(message);
  }

  public InternalRpcTokenException(@Nullable final String message,
                                   @Nullable final Throwable cause) {
    super(message, cause);
  }

}