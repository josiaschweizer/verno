package ch.verno.common.gate.properties;

import ch.verno.common.lib.application.RunMode;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ApplicationPropertiesGate {

  @Nonnull
  RunMode getRunMode();

  @Nonnull
  String getSubscriptionOverviewUrl();

  @Nonnull
  String getCheckoutCancelUrl();

  @Nonnull
  String getCheckoutSuccessUrl();

  @Nonnull
  String getPortalReturnUrl();

  boolean isTenantEnabled();

  @Nonnull
  String getTenantHeaderName();

  @Nonnull
  List<String> getTenantBaseDomains();

  boolean isTenantAllowHeaderFallback();
}
