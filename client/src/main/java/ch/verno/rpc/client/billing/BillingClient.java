package ch.verno.rpc.client.billing;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.contract.endpoint.billing.BillingResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class BillingClient {

  @Nonnull private final Lazy<BillingResource> billingResource;

  @Inject
  public BillingClient(@Nonnull final RpcFactory rpcFactory) {
    this.billingResource = Lazy.of(() -> rpcFactory.create(BillingResource.class));
  }

  public boolean isTenantBillingOptionLicenced(@Nonnull final BillingLicenceOption option) {
    return billingResource.get().isOptionLicenced(option);
  }

  @Nonnull
  public TenantBillingDto getTenantBillingForCurrentTenant() {
    return billingResource.get().getTenantBillingForCurrentTenant();
  }

  public void createTenantBilling(@Nonnull final TenantBillingDto tenantBillingDto) {
    billingResource.get().createTenantBilling(tenantBillingDto);
  }

  @Nonnull
  public String createSubscriptionUrlForCheckout(@Nonnull final Long userId) {
    return billingResource.get().createSubscriptionUrlForCheckout(userId);
  }

  @Nonnull
  public String getSubscriptionOverviewUrl() {
    return billingResource.get().getSubscriptionOverviewUrl();
  }

  public boolean hasValidSubscriptionByTenantId(@Nonnull final Long tenantId){
   return  billingResource.get().hasValidSubscriptionByTenantId(tenantId);
  }

}
