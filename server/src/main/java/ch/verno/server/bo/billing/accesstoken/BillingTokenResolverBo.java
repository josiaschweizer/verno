package ch.verno.server.bo.billing.accesstoken;

import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.exception.stripe.StripeTokenException;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.billing.BillingAccessTokenService;
import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;
import java.util.Optional;

public class BillingTokenResolverBo {

  @Nonnull private final Lazy<BillingAccessTokenService> billingAccessTokenService;

  @Nonnull private final TokenHashUtil hashTokenUtil;

  protected BillingTokenResolverBo(@Nonnull final ServerBean serverBean) {
    this.billingAccessTokenService = Lazy.of(() -> serverBean.get(BillingAccessTokenService.class));

    this.hashTokenUtil = new TokenHashUtil();
  }

  /**
   * Resolves the billing access token and directly marks it as used.
   *
   * @param rawToken the raw token of the billing access token returned when creating the billing access token
   * @return the billing access token dto
   */
  @Nonnull
  public BillingAccessTokenDto resolveAndMarkBillingAccessTokenAsUsed(@Nonnull final String rawToken) {
    final var token = resolveBillingAccessToken(rawToken);

    return billingAccessTokenService.get().markAsUsed(token.getTokenHash());
  }

  /**
   * Resolves the billing access token by hashing the raw token and checking the hash against the stored token hash.
   *
   * @param rawToken the raw token of the billing access token returned when creating the billing access token
   * @return the billing access token dto
   * @throws StripeTokenException if the rawToken is blank, expired, or already used
   */
  @Nonnull
  public BillingAccessTokenDto resolveBillingAccessToken(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      throw new StripeTokenException("raw token cannot be blank");
    }

    final var token = findTokenByRawToken(rawToken);
    validateBillingAccessToken(token);

    return token;
  }

  /**
   * Checks whether the given raw token resolves to a valid, unused, unexpired billing access token.
   *
   * @param rawToken the raw token of the billing access token returned when creating the billing access token
   * @return true if the token is valid, false otherwise
   */
  public boolean isBillingAccessTokenValid(@Nonnull final String rawToken) {
    return resolveBillingAccessTokenSafe(rawToken).isPresent();
  }

  /**
   * Resolves the billing access token safely without throwing a {@link StripeTokenException}.
   *
   * @param rawToken the raw token of the billing access token returned when creating the billing access token
   * @return an Optional containing the billing access token dto if it is valid, otherwise an empty Optional
   */
  @Nonnull
  public Optional<BillingAccessTokenDto> resolveBillingAccessTokenSafe(@Nonnull final String rawToken) {
    if (rawToken.isBlank()) {
      return Optional.empty();
    }

    final var token = findTokenByRawToken(rawToken);

    return isTokenValid(token) ? Optional.of(token) : Optional.empty();
  }

  @Nonnull
  private BillingAccessTokenDto findTokenByRawToken(@Nonnull final String rawToken) {
    final var tokenHash = hashTokenUtil.hashToken(rawToken);

    return billingAccessTokenService.get().findByTokenHash(tokenHash);
  }

  private void validateBillingAccessToken(@Nonnull final BillingAccessTokenDto token) {
    if (isExpired(token)) {
      throw new StripeTokenException("Billing access token is expired");
    }

    if (isUsed(token)) {
      throw new StripeTokenException("Billing access token has already been used");
    }
  }

  private boolean isTokenValid(@Nonnull final BillingAccessTokenDto token) {
    return !isExpired(token) && !isUsed(token);
  }

  private boolean isExpired(@Nonnull final BillingAccessTokenDto token) {
    return token.getExpiresAt() != null && token.getExpiresAt().isBefore(OffsetDateTime.now());
  }

  private boolean isUsed(@Nonnull final BillingAccessTokenDto token) {
    return token.getUsedAt() != null;
  }
}