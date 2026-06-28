package ch.verno.server.rpc.resource.billing;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.contract.endpoint.billing.BillingResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.application.properties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.billing.BillingBo;
import ch.verno.server.service.extern.billing.TenantBillingService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(BillingResource.class)
public class BillingResourceImpl implements BillingResource {

  @Nonnull private final Lazy<BillingBo> billingBo;
  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<BillingConfigProvider> billingConfigProvider;

  public BillingResourceImpl(@Nonnull final ServerBean serverBean) {
    this.billingBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(BillingBo.class));
    this.tenantBillingService = Lazy.of(() -> serverBean.get(TenantBillingService.class));
    this.billingConfigProvider = Lazy.of(() -> serverBean.get(BillingConfigProvider.class));
  }

  @Override
  public boolean isOptionLicenced(@Nonnull final BillingLicenceOption option) {
    return billingBo.get().isOptionLicenced(option);
  }

  @Nonnull
  @Override
  public TenantBillingDto getTenantBillingForCurrentTenant() {
    return billingBo.get().getTenantBillingForCurrentTenant();
  }

  @Override
  public void createTenantBilling(@Nonnull final TenantBillingDto dto) {
    tenantBillingService.get().save(dto);
  }

  @Nonnull
  @Override
  public String createSubscriptionUrlForCheckout(@Nonnull final Long userId) {
    return billingBo.get().createSubscriptionUrlForCheckout(userId);
  }

  @Nonnull
  @Override
  public String getSubscriptionOverviewUrl() {
    return billingConfigProvider.get().getSubscriptionOverviewUrl();
  }

  @Override
  public boolean hasValidSubscriptionByTenantId(@Nonnull final Long tenantId) {
    final var subscriptionOptional = tenantBillingService.get().findOptionalByTenantId(tenantId);
    return subscriptionOptional.filter(tenantBillingDto -> billingBo.get().isSubscriptionValid(tenantBillingDto)).isPresent();
  }
}
