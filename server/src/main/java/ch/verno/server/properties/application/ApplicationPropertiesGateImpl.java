package ch.verno.server.properties.application;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.properties.ApplicationPropertiesGate;
import ch.verno.common.lib.application.RunMode;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationPropertiesGateImpl implements ApplicationPropertiesGate {

  @Nonnull
  private final TenantProperties tenantProperties;
  @Nonnull
  private final BillingProperties billingProperties;
  @Nonnull
  private final ApplicationProperties applicationProperties;

  public ApplicationPropertiesGateImpl(@Nonnull final GlobalInterface globalInterface) {
    tenantProperties = globalInterface.getService(TenantProperties.class);
    billingProperties = globalInterface.getService(BillingProperties.class);
    applicationProperties = globalInterface.getService(ApplicationProperties.class);
  }

  @Nonnull
  @Override
  public RunMode getRunMode() {
    final var runModeKey = applicationProperties.getRunMode();
    if (runModeKey.isBlank()) {
      return RunMode.PROD;
    }

    return RunMode.fromKey(runModeKey);
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

  @Override
  public boolean isTenantEnabled() {
    return tenantProperties.isEnabled();
  }

  @Nonnull
  @Override
  public String getTenantHeaderName() {
    return tenantProperties.getHeaderName();
  }

  @Nonnull
  @Override
  public List<String> getTenantBaseDomains() {
    return tenantProperties.getBaseDomains();
  }

  @Override
  public boolean isTenantAllowHeaderFallback() {
    return tenantProperties.isAllowHeaderFallback();
  }
}
