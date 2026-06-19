package ch.verno.server.bo.billing;

import ch.verno.common.tenant.TenantContext;
import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.extern.billing.TenantBillingService;
import jakarta.annotation.Nonnull;

import java.util.List;

public class BillingBo {

  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;

  protected BillingBo(@Nonnull final ServerBean bean) {
    this.tenantBillingService = Lazy.of(() -> bean.get(TenantBillingService.class));
  }

  public boolean isOptionLicenced(@Nonnull final BillingLicenceOption option) {
    return getTenantLicenceOptions().contains(option);
  }

  @Nonnull
  public List<BillingLicenceOption> getTenantLicenceOptions() {
    final var currentTenant = TenantContext.get();
    if (currentTenant == null) {
      return New.arrayList();
    }

    final var billing = tenantBillingService.get().findByTenantId(currentTenant);
    final var planOptions = billing.getPlanKey().getBillingLicenceOptions();
    final var additionalOptions = billing.getAdditionalLicenceOptions();

    return New.arrayList(planOptions, additionalOptions);
  }
}
