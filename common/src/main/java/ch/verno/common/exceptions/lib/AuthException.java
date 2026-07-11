package ch.verno.common.exceptions.lib;

import jakarta.annotation.Nonnull;

public class AuthException extends RuntimeException {

  public AuthException(@Nonnull final String message) {
    super(message);
  }

}
