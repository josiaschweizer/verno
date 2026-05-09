package ch.verno.server.properties.billing;

import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.type.billing.BillingLicenceOption;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.properties.BillingProperties;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillingPropertiesImpl implements BillingProperties {

  @Nonnull private final Lazy<ITenantBillingService> tenantBillingService;

  public BillingPropertiesImpl(@Nonnull final GlobalInterface globalInterface) {
    this.tenantBillingService = Lazy.of(() -> globalInterface.getService(ITenantBillingService.class));
  }

  @Override
  public boolean isOptionLicenced(@Nonnull final BillingLicenceOption option) {
    return getTenantLicenceOptions().contains(option);
  }

  @Nonnull
  @Override
  public List<BillingLicenceOption> getTenantLicenceOptions() {
    final var currentTenant = TenantContext.get();
    if (currentTenant == null) {
      return New.arrayList();
    }

    final var billing = tenantBillingService.get().getTenantBillingByTenantId(currentTenant);
    final var planOptions = billing.getPlanKey().getBillingLicenceOptions();
    final var additionalOptions = billing.getAdditionalLicenceOptions();

    return New.arrayList(planOptions, additionalOptions);
  }

}
