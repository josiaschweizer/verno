package ch.verno.common.exceptions.io;

import jakarta.annotation.Nonnull;

public class ParseCsvException extends RuntimeException {

  public ParseCsvException(@Nonnull final String message,
                           @Nonnull final Exception cause) {
    super(message);
  }

}
