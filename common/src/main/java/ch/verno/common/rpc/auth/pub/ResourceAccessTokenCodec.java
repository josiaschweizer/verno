package ch.verno.common.rpc.auth.pub;

import ch.verno.lib.exception.rpc.auth.ResourceAccessTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Issues and verifies short-lived, single-resource-scoped access tokens used
 * by unauthenticated-but-token-gated public endpoints
 * (see {@code ApiUrl.PUBLIC_AUTH_BASE_API}).
 */
public class ResourceAccessTokenCodec {

  @Nonnull private static final String CLAIM_TENANT_ID = "tid";
  @Nonnull private static final String CLAIM_RESOURCE = "res";

  @Nonnull private final SecretKey signingKey;
  @Nonnull private final Duration ttl;

  public ResourceAccessTokenCodec(@Nonnull final String secret,
                                  @Nonnull final Duration ttl) {
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    this.ttl = ttl;
  }

  /**
   * Mints a token authorizing access to {@code resourceId} within {@code tenantId}
   * for this codec's configured TTL.
   */
  @Nonnull
  public String issue(@Nonnull final Long tenantId,
                      @Nonnull final String resourceId) {
    final var now = Instant.now();

    return Jwts.builder()
            .claim(CLAIM_TENANT_ID, tenantId)
            .claim(CLAIM_RESOURCE, resourceId)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(ttl)))
            .signWith(signingKey)
            .compact();
  }

  /**
   * Verifies the token's signature and expiry, and confirms it was issued for
   * exactly {@code expectedResourceId}.
   *
   * @throws ResourceAccessTokenException if the token is invalid, expired, or
   *                                      does not match the requested resource
   */
  @Nonnull
  public ResourceAccessPrincipal verify(@Nonnull final String token,
                                        @Nonnull final String expectedResourceId) {
    final Claims claims;
    try {
      claims = Jwts.parser()
              .verifyWith(signingKey)
              .build()
              .parseSignedClaims(token)
              .getPayload();
    } catch (final JwtException | IllegalArgumentException exception) {
      throw new ResourceAccessTokenException("Invalid resource access token", exception);
    }

    final var resource = claims.get(CLAIM_RESOURCE, String.class);
    if (resource == null || !resource.equals(expectedResourceId)) {
      throw new ResourceAccessTokenException("Resource access token does not match requested resource");
    }

    final var tenantId = claims.get(CLAIM_TENANT_ID, Long.class);
    if (tenantId == null) {
      throw new ResourceAccessTokenException("Resource access token missing tenant claim");
    }

    return new ResourceAccessPrincipal(tenantId, resource);
  }

  public record ResourceAccessPrincipal(@Nonnull Long tenantId,
                                        @Nonnull String resourceId) {

  }
}