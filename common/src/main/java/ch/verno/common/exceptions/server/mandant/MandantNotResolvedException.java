package ch.verno.common.exceptions.server.mandant;

public class MandantNotResolvedException extends RuntimeException {

  public MandantNotResolvedException(final String message) {
    super(message);
  }

  public MandantNotResolvedException(final String message, final Throwable cause) {
    super(message, cause);
  }
}