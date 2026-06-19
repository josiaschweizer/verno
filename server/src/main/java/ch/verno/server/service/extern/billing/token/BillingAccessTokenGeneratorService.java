package ch.verno.server.service.extern.billing.token;

import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.contract.dto.lib.billing.GeneratedBillingAccessTokenDto;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.extern.billing.BillingAccessTokenService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

@Service
public class BillingAccessTokenGeneratorService {

  private static final int TOKEN_BYTE_LENGTH = 32;
  private static final int DEFAULT_EXPIRY_MINUTES = 10;

  @Nonnull private final SecureRandom secureRandom;
  @Nonnull private final Lazy<BillingAccessTokenService> billingAccessTokenService;

  public BillingAccessTokenGeneratorService(@Nonnull final ServerBean bean) {
    this.secureRandom = new SecureRandom();
    this.billingAccessTokenService = Lazy.of(() -> bean.get(BillingAccessTokenService.class));
  }

  @Nonnull
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
  @Transactional
  public GeneratedBillingAccessTokenDto generateBillingAccessToken(final long tenantId,
                                                                   final long userId,
                                                                   @Nonnull final BillingAccessTokenPurpose purpose,
                                                                   @Nonnull final OffsetDateTime expiresAt) {
    final var rawToken = generateRawToken();
    final var tokenHash = hashToken(rawToken);

    final var dto = BillingAccessTokenDto.empty();
    dto.setTenantId(tenantId);
    dto.setUserId(userId);
    dto.setTokenHash(tokenHash);
    dto.setPurpose(purpose.name());
    dto.setExpiresAt(expiresAt);

    final var savedToken = billingAccessTokenService.get().save(dto);
    return new GeneratedBillingAccessTokenDto(rawToken, savedToken);
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