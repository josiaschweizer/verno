package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.server.service.extern.billing.token.IBillingAccessTokenResolverService;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class BillingAccessTokenResolverService implements IBillingAccessTokenResolverService {

  @Nonnull
  private final BillingAccessTokenService billingAccessTokenService;

  @Nonnull
  private final BillingAccessTokenGeneratorService billingAccessTokenGeneratorService;

  public BillingAccessTokenResolverService(@Nonnull final BillingAccessTokenService billingAccessTokenService,
                                           @Nonnull final BillingAccessTokenGeneratorService billingAccessTokenGeneratorService) {
    this.billingAccessTokenService = billingAccessTokenService;
    this.billingAccessTokenGeneratorService = billingAccessTokenGeneratorService;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingAccessTokenDto resolveBillingAccessToken(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      throw new IllegalStateException("rawToken must not be blank");
    }

    final var tokenHash = billingAccessTokenGeneratorService.hashToken(rawToken);
    final var token = billingAccessTokenService.getBillingAccessTokenByTokenHash(tokenHash);

    validateBillingAccessToken(token);

    return token;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingAccessTokenDto resolveBillingAccessToken(@Nonnull final String rawToken,
                                                         @Nonnull final BillingAccessTokenPurpose expectedPurpose) {
    final var token = resolveBillingAccessToken(rawToken);

    if (!expectedPurpose.name().equals(token.getPurpose())) {
      throw new IllegalStateException("Billing access token has invalid purpose");
    }

    return token;
  }

  @Nonnull
  @Override
  @Transactional
  public BillingAccessTokenDto resolveAndMarkBillingAccessTokenAsUsed(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      throw new IllegalStateException("rawToken must not be blank");
    }

    final var tokenHash = billingAccessTokenGeneratorService.hashToken(rawToken);
    final var token = billingAccessTokenService.getBillingAccessTokenByTokenHash(tokenHash);

    validateBillingAccessToken(token);

    return billingAccessTokenService.markBillingAccessTokenAsUsed(tokenHash);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isBillingAccessTokenValid(@Nonnull final String rawToken) {
    try {
      resolveBillingAccessToken(rawToken);
      return true;
    } catch (RuntimeException exception) {
      return false;
    }
  }

  private void validateBillingAccessToken(@Nonnull final BillingAccessTokenDto token) {
    if (token.getExpiresAt() != null && token.getExpiresAt().isBefore(OffsetDateTime.now())) {
      throw new IllegalStateException("Billing access token is expired");
    }

    if (token.getUsedAt() != null) {
      throw new IllegalStateException("Billing access token has already been used");
    }
  }
}