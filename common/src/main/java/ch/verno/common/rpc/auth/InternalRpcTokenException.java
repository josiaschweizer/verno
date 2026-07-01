package ch.verno.common.rpc.auth;

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