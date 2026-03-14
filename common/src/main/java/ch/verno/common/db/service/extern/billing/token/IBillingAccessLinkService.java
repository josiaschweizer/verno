package ch.verno.common.db.service.extern.billing.token;

import jakarta.annotation.Nonnull;

public interface IBillingAccessLinkService {

  @Nonnull
  String createSubscriptionOverviewUrl(@Nonnull Long tenantId,
                                       @Nonnull  Long userId);
}
