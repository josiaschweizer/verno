package ch.verno.lib.exception.entity;

import jakarta.annotation.Nullable;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(@Nullable final String message) {
    super(message);
  }

}
