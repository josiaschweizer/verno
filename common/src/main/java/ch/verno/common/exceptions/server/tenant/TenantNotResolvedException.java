package ch.verno.common.exceptions.server.tenant;

public class TenantNotResolvedException extends RuntimeException {

  public TenantNotResolvedException(final String message) {
    super(message);
  }

  public TenantNotResolvedException(final String message, final Throwable cause) {
    super(message, cause);
  }
}