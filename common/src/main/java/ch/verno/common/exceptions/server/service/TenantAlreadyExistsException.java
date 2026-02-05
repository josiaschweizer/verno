package ch.verno.common.exceptions.server.service;


import jakarta.annotation.Nonnull;

public class TenantAlreadyExistsException extends RuntimeException {

  public TenantAlreadyExistsException(@Nonnull final String message) {
    super(message);
  }

}
