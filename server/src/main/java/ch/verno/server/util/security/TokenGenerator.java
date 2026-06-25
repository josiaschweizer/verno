package ch.verno.server.util.security;

import ch.verno.lib.Lazy;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class TokenGenerator {

  private static final int TOKEN_BYTE_LENGTH = 32;

  @Nonnull private final Lazy<SecureRandom> secureRandom;

  public TokenGenerator() {
    this.secureRandom = Lazy.of(SecureRandom::new);
  }

  @Nonnull
  public String generateRawToken() {
    final var tokenBytes = new byte[TOKEN_BYTE_LENGTH];
    secureRandom.get().nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

  @Nonnull
  public String hashToken(@Nonnull final String rawToken) {
    try {
      final var digest = MessageDigest.getInstance("SHA-256");
      final var hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 algorithm not available", exception);
    }
  }

}
