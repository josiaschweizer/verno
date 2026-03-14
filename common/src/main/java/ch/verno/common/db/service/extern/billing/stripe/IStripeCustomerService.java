package ch.verno.common.db.service.extern.billing.stripe;

import jakarta.annotation.Nonnull;

public interface IStripeCustomerService {

  @Nonnull
  String getOrCreateCustomer(@Nonnull Long tenantId,
                             @Nonnull Long userId);

}
