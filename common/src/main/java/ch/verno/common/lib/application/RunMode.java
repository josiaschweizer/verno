package ch.verno.common.lib.application;

import jakarta.annotation.Nonnull;

public enum RunMode {
  DEV("dev"),
  INT("int"),
  PROD("prod"),
  ;

  @Nonnull
  private final String key;

  RunMode(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public static RunMode fromKey(@Nonnull final String key) {
    for (final var value : values()) {
      if (value.getKey().equals(key)) {
        return value;
      }
    }

    throw new IllegalArgumentException("invalid run mode key");
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}
