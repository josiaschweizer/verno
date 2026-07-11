package ch.verno.ui.base.os;

import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;

public enum OS {
  WINDOWS,
  MAC,
  LINUX,
  ANDROID,
  IOS,
  MOBILE,
  ;

  @Nonnull
  public static OS fromUserAgent(@Nonnull final String rawUserAgent) {
    final String userAgent = rawUserAgent.toLowerCase();

    if (userAgent.contains(VernoConstants.IPHONE) ||
            userAgent.contains(VernoConstants.IPAD) ||
            userAgent.contains(VernoConstants.IPOD) ||
            userAgent.contains(VernoConstants.IOS)) {
      return IOS;
    }

    if (userAgent.contains(VernoConstants.ANDROID)) {
      return ANDROID;
    }

    if (userAgent.contains(VernoConstants.MOBILE)) {
      return MOBILE;
    }

    if (userAgent.contains(VernoConstants.WINDOWS)) {
      return WINDOWS;
    }

    if (userAgent.contains(VernoConstants.MAC_OS_X) ||
            userAgent.contains(VernoConstants.MACINTOSH) ||
            userAgent.contains(VernoConstants.DARWIN)) {
      return MAC;
    }

    if (userAgent.contains(VernoConstants.LINUX) ||
            userAgent.contains(VernoConstants.X_11)) {
      return LINUX;
    }

    return getDefault();
  }

  @Nonnull
  public static OS getDefault() {
    return WINDOWS;
  }

  public boolean isMobile() {
    return this == IOS || this == ANDROID || this == MOBILE;
  }
}