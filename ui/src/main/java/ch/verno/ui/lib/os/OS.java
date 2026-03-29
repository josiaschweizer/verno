package ch.verno.ui.lib.os;

import jakarta.annotation.Nonnull;

public enum OS {
  WINDOWS("windows"),
  MAC("mac"),
  LINUX("linux"),
  ;

  @Nonnull private final String key;

  OS(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public static OS getFromKey(@Nonnull final String key) {
    final var normalizedKey = key.toLowerCase();

    for (final var value : values()) {
      if (normalizedKey.contains(value.getKey())) {
        return value;
      }
    }

    return WINDOWS;
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}
