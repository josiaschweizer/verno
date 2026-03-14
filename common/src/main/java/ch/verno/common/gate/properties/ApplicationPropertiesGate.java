package ch.verno.common.gate.properties;

import jakarta.annotation.Nonnull;

public interface ApplicationPropertiesGate {

  @Nonnull
  String getSubscriptionOverviewUrl();

  @Nonnull
  String getCheckoutCancelUrl();

  @Nonnull
  String getCheckoutSuccessUrl();

  @Nonnull
  String getPortalReturnUrl();

}
