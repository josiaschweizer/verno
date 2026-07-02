package ch.verno.lib.exception.stripe;

import jakarta.annotation.Nonnull;

public class StripeTokenException extends RuntimeException {

  public StripeTokenException(@Nonnull final String message) {
    super(message);
  }

}
