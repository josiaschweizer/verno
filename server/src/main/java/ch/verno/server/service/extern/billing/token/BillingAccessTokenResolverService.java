package ch.verno.server.service.extern.billing.token;

import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.extern.billing.BillingAccessTokenService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class BillingAccessTokenResolverService {

  @Nonnull private final Lazy<BillingAccessTokenService> billingAccessTokenService;
  @Nonnull private final Lazy<BillingAccessTokenGeneratorService> tokenGeneratorService;

  public BillingAccessTokenResolverService(@Nonnull final ServerBean bean) {
    this.billingAccessTokenService = Lazy.of(() -> bean.get(BillingAccessTokenService.class));
    this.tokenGeneratorService = Lazy.of(() -> bean.get(BillingAccessTokenGeneratorService.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public BillingAccessTokenDto resolveBillingAccessToken(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      throw new IllegalStateException("rawToken must not be blank");
    }

    final var tokenHash = tokenGeneratorService.get().hashToken(rawToken);
    final var token = billingAccessTokenService.get().findByTokenHash(tokenHash);

    validateBillingAccessToken(token);

    return token;
  }

  @Nonnull
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
  @Transactional
  public BillingAccessTokenDto resolveAndMarkBillingAccessTokenAsUsed(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      throw new IllegalStateException("rawToken must not be blank");
    }

    final var tokenHash = tokenGeneratorService.get().hashToken(rawToken);
    final var token = billingAccessTokenService.get().findByTokenHash(tokenHash);

    validateBillingAccessToken(token);

    return billingAccessTokenService.get().markAsUsed(tokenHash);
  }

  @Transactional(readOnly = true)
  public boolean isBillingAccessTokenValid(@Nonnull final String rawToken) {
    try {
      resolveBillingAccessToken(rawToken);
      return true;
    } catch (final RuntimeException exception) {
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