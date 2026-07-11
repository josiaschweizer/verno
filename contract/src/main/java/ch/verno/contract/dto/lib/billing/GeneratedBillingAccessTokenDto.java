package ch.verno.contract.dto.lib.billing;

import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
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