package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.dto.billing.GeneratedBillingAccessTokenDto;
import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.service.extern.billing.token.IBillingAccessTokenGeneratorService;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import jakarta.annotation.Nonnull;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

@Service
public class BillingAccessTokenGeneratorService implements IBillingAccessTokenGeneratorService {

  private static final int TOKEN_BYTE_LENGTH = 32;
  private static final int DEFAULT_EXPIRY_MINUTES = 10;

  @Nonnull private final SecureRandom secureRandom;
  @Nonnull private final BillingAccessTokenService billingAccessTokenService;

  public BillingAccessTokenGeneratorService(@Nonnull final BillingAccessTokenService billingAccessTokenService) {
    this.billingAccessTokenService = billingAccessTokenService;
    this.secureRandom = new SecureRandom();
  }

  @Nonnull
  @Override
  @Transactional
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(final long tenantId,
                                                                   final long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose) {
    return generateBillingAccessToken(
            tenantId,
            userId,
            purpose,
            OffsetDateTime.now().plusMinutes(DEFAULT_EXPIRY_MINUTES)
    );
  }

  @Nonnull
  @Override
  @Transactional
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(final long tenantId,
                                                                   final long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose,
                                                                   @Nonnull final OffsetDateTime expiresAt) {
    final var rawToken = generateRawToken();
    final var tokenHash = hashToken(rawToken);

    final var dto = new BillingAccessTokenDto();
    dto.setTenantId(tenantId);
    dto.setUserId(userId);
    dto.setTokenHash(tokenHash);
    dto.setPurpose(purpose.name());
    dto.setExpiresAt(expiresAt);

    final var savedToken = billingAccessTokenService.createBillingAccessToken(dto);
    return new GeneratedBillingAccessTokenDto(rawToken, savedToken);
  }

  @Nonnull
  @Override
  public String hashToken(@Nonnull final String rawToken) {
    try {
      final var digest = MessageDigest.getInstance("SHA-256");
      final var hashBytes = digest.digest(rawToken.getBytes(StandardCharsets.UTF_8));
      return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 algorithm not available", exception);
    }
  }

  @Nonnull
  @Override
  public String generateRawToken() {
    final var tokenBytes = new byte[TOKEN_BYTE_LENGTH];
    secureRandom.nextBytes(tokenBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
  }

}
