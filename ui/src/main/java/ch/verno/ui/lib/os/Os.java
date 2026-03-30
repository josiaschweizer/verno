package ch.verno.ui.lib.os;

import jakarta.annotation.Nonnull;

public enum Os {
  WINDOWS("windows"),
  MAC("mac"),
  LINUX("linux"),
  ;

  @Nonnull private final String key;

  Os(@Nonnull final String key) {
    this.key = key;
  }


  @Nonnull
  public static Os fromUserAgent(@Nonnull final String rawUserAgent) {
    final var userAgent = rawUserAgent.toLowerCase();

    if (userAgent.contains("windows")) {
      return WINDOWS;
    }
    if (userAgent.contains("mac os x") ||
            userAgent.contains("macintosh") ||
            userAgent.contains("darwin")) {
      return MAC;
    }
    if (userAgent.contains("linux") ||
            userAgent.contains("x11")) {
      return LINUX;
    }

    return getDefault();
  }

  @Nonnull
  public static Os getDefault() {
    return Os.WINDOWS;
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}
