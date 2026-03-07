package ch.verno.common.exceptions.lib;

import jakarta.annotation.Nonnull;

public class EnvironmentVariableNotFound extends IllegalArgumentException {

  public EnvironmentVariableNotFound(@Nonnull final String message) {
    super(message);
  }

}
