package ch.verno.common.exceptions.server.mail;

import jakarta.annotation.Nonnull;

public class SendMailException extends RuntimeException {

  public SendMailException(@Nonnull final String message) {
    super(message);
  }

  public SendMailException(@Nonnull final String message,
                           @Nonnull final Throwable cause) {
    super(message, cause);
  }

}
