package ch.verno.common.exceptions.server.service;

import jakarta.annotation.Nonnull;

public class TenantProvisionFailedException extends RuntimeException {

  public TenantProvisionFailedException(@Nonnull final String message, @Nonnull final Throwable cause) {
    super(message, cause);
  }

}