package ch.verno.lib.exception;

import jakarta.annotation.Nonnull;

public final class StripeSessionException extends RuntimeException {

  public StripeSessionException(@Nonnull final String message) {
    super(message);
  }

  public StripeSessionException(@Nonnull final Throwable cause) {
    super(cause);
  }

  public StripeSessionException(@Nonnull final String message,
                                @Nonnull final Throwable cause) {
    super(message, cause);
  }

}
