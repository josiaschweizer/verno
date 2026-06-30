package ch.verno.lib.exception.file;

import jakarta.annotation.Nonnull;

public class FileUploadException extends RuntimeException {

  public FileUploadException(@Nonnull final String message,
                             @Nonnull final Throwable cause) {
    super(message, cause);
  }


}
