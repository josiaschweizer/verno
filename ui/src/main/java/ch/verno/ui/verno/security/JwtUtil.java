package ch.verno.ui.verno.security;


import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

  private static final String TENANT_ID = "tenantId";
  private static final String ROLE = "role";
  private static final String USER_ID = "userId";

  private final long expirationMs;

  private final Key signingKey;

  @Autowired
  public JwtUtil(@Value("${jwt.secret}") @Nonnull final String secret,
                 @Value("${jwt.expiration-ms}") final long expirationMs) {
    this.expirationMs = expirationMs;
    this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
  }

  @Nonnull
  public String generateToken(@Nonnull final CustomUserPasswordAuthenticationToken token) {
    final var user = (AppUserDto) token.getPrincipal();
    final var now = new Date();
    final var expires = new Date(now.getTime() + expirationMs);

    if (user == null) {
      throw new IllegalArgumentException("User token is null");
    }

    return Jwts.builder()
            .subject(user.getUsername())
            .claim(TENANT_ID, user.getTenant())
            .claim(ROLE, user.getRole())
            .claim(USER_ID, user.getId())
            .issuedAt(now)
            .expiration(expires)
            .signWith(signingKey)
            .compact();
  }

  @Nonnull
  public String extractEmail(@Nonnull final String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public long extractTenantId(@Nonnull final String token) {
    return extractClaim(token, claims -> claims.get(TENANT_ID, Number.class)).longValue();
  }

  @Nonnull
  public Role extractRole(@Nonnull final String token) {
    return extractClaim(token, claims -> Role.valueOf(claims.get(ROLE, String.class)));
  }

  public long extractUserId(@Nonnull final String token) {
    return extractClaim(token, claims -> claims.get(USER_ID, Number.class)).longValue();
  }

  public boolean validateToken(@Nonnull final String token) {
    try {
      final var claims = extractAllClaims(token);

      return !claims.getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  @Nonnull
  private <T> T extractClaim(@Nonnull final String token,
                             @Nonnull final Function<Claims, T> resolver) {
    return resolver.apply(extractAllClaims(token));
  }

  @Nonnull
  private Claims extractAllClaims(@Nonnull final String token) {
    return Jwts.parser()
            .verifyWith((SecretKey) signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
  }
}