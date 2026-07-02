package ch.verno.lib.exception.rpc.auth;

import jakarta.annotation.Nonnull;

public class ResourceAccessTokenException extends RuntimeException {

  public ResourceAccessTokenException(@Nonnull final String message) {
    super(message);
  }

  public ResourceAccessTokenException(@Nonnull final String message,
                                      @Nonnull final Throwable cause) {
    super(message, cause);
  }
}