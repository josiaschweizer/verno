package ch.verno.server.bo.billing.accesstoken;

import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenHashUtil {

  private static final int TOKEN_BYTE_LENGTH = 32;

  @Nonnull private final SecureRandom secureRandom;

  public TokenHashUtil() {
    this.secureRandom = new SecureRandom();
  }

  @Nonnull
  public String hashToken(@Nonnull final String rawToken) {
    try {
      final var digest = MessageDigest.getInstance("SHA-256");
      final var hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
    } catch (final NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 algorithm not available", exception);
    }
  }

  @Nonnull
  public String generateRawToken() {
    final var tokenBytes = new byte[TOKEN_BYTE_LENGTH];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

}
