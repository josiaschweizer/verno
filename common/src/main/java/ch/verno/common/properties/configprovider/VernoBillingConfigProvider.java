package ch.verno.common.properties.configprovider;

import jakarta.annotation.Nonnull;

public interface VernoBillingConfigProvider {

  @Nonnull
  String getSubscriptionOverviewUrl();

  @Nonnull
  String getCheckoutCancelUrl();

  @Nonnull
  String getCheckoutSuccessUrl();

  @Nonnull
  String getPortalReturnUrl();

}
