package ch.verno.common.db.dto.billing;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;

public record GeneratedBillingAccessTokenDto(@Nonnull String rawToken,
                                             @Nonnull BillingAccessTokenDto billingAccessToken) {

  @Nullable
  public OffsetDateTime getExpiresAt() {
    return billingAccessToken.getExpiresAt();
  }
}