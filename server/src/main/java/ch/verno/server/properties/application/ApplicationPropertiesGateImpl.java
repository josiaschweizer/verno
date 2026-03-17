package ch.verno.server.properties.application;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.properties.ApplicationPropertiesGate;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class ApplicationPropertiesGateImpl implements ApplicationPropertiesGate {

  private final BillingProperties billingProperties;

  public ApplicationPropertiesGateImpl(@Nonnull final GlobalInterface globalInterface) {
    billingProperties = globalInterface.getService(BillingProperties.class);
  }

  @Nonnull
  @Override
  public String getSubscriptionOverviewUrl() {
    return billingProperties.getSubscriptionOverviewUrl();
  }

  @Nonnull
  @Override
  public String getCheckoutCancelUrl() {
    return billingProperties.getCheckoutCancelUrl();
  }

  @Nonnull
  @Override
  public String getCheckoutSuccessUrl() {
    return billingProperties.getCheckoutSuccessUrl();
  }

  @Nonnull
  @Override
  public String getPortalReturnUrl() {
    return billingProperties.getPortalReturnUrl();
  }

}
