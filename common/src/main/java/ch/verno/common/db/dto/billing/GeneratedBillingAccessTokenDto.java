package ch.verno.common.db.dto.billing;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;

public class GeneratedBillingAccessTokenDto {

  @Nonnull
  private final String rawToken;

  @Nonnull
  private final BillingAccessTokenDto billingAccessToken;

  public GeneratedBillingAccessTokenDto(@Nonnull final String rawToken,
                                        @Nonnull final BillingAccessTokenDto billingAccessToken) {
    this.rawToken = rawToken;
    this.billingAccessToken = billingAccessToken;
  }

  @Nonnull
  public String getRawToken() {
    return rawToken;
  }

  @Nonnull
  public BillingAccessTokenDto getBillingAccessToken() {
    return billingAccessToken;
  }

  @Nullable
  public OffsetDateTime getExpiresAt() {
    return billingAccessToken.getExpiresAt();
  }
}