package ch.verno.common.rpc.auth.internal;

import ch.verno.lib.exception.rpc.auth.internal.InternalRpcTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public final class InternalRpcTokenCodec {

  @Nonnull private static final String CLAIM_TENANT_ID = "tenantId"; //TODO use X-Mandnat?

  @Nonnull private final SecretKey secretKey;

  public InternalRpcTokenCodec(@Nonnull final String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  @Nonnull
  public String issue(@Nonnull final String username,
                      @Nullable final Long tenantId,
                      @Nonnull final Duration ttl) {
    final Instant now = Instant.now();

    final var builder = Jwts.builder()
            .subject(username)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(ttl)));

    if (tenantId != null) {
      builder.claim(CLAIM_TENANT_ID, tenantId);
    }

    return builder.signWith(secretKey).compact();
  }

  @Nonnull
  public InternalRpcPrincipal verify(@Nonnull final String token) {
    try {
      final Claims claims = Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(token)
              .getPayload();

      final String username = claims.getSubject();
      if (username == null || username.isBlank()) {
        throw new InternalRpcTokenException("Internal RPC token has no subject");
      }

      final Long tenantId = claims.get(CLAIM_TENANT_ID, Long.class);

      return new InternalRpcPrincipal(username, tenantId);
    } catch (final JwtException exception) {
      throw new InternalRpcTokenException("Invalid internal RPC token: " + exception.getMessage());
    }
  }
}