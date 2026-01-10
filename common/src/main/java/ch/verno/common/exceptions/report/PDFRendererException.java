package ch.verno.common.exceptions.report;

import jakarta.annotation.Nonnull;

public class PDFRendererException extends RuntimeException {

  public PDFRendererException(@Nonnull final String message) {
    super(message);
  }

  public PDFRendererException(@Nonnull final String message,
                              @Nonnull final Throwable cause) {
    super(message, cause);
  }

}
