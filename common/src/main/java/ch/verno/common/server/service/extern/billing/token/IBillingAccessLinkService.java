package ch.verno.common.server.service.extern.billing.token;

import jakarta.annotation.Nonnull;

public interface IBillingAccessLinkService {

  @Nonnull
  String createSubscriptionUrlForCheckout(@Nonnull Long tenantId,
                                          @Nonnull  Long userId);
}
