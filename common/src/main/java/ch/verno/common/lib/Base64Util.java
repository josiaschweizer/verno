package ch.verno.common.lib;

import jakarta.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Util {

  private Base64Util() {}

  public static String encode(@Nonnull final byte[] data) {
    return Base64.getEncoder().encodeToString(data);
  }

  public static byte[] decode(@Nonnull final String base64String) {
    return Base64.getDecoder().decode(base64String);
  }

  public static String encodeString(@Nonnull final String value) {
    return encode(value.getBytes(StandardCharsets.UTF_8));
  }

  public static String decodeToString(@Nonnull final String base64String) {
    return new String(decode(base64String), StandardCharsets.UTF_8);
  }
}