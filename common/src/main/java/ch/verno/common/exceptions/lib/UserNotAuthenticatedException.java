package ch.verno.common.exceptions.lib;

import jakarta.annotation.Nonnull;

public class UserNotAuthenticatedException extends AuthException {

  public UserNotAuthenticatedException(@Nonnull final String message) {
    super(message);
  }

}
