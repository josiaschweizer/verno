package ch.verno.contract.endpoint.billing;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface BillingResource {

  boolean isOptionLicenced(@Nonnull BillingLicenceOption option);

  @Nonnull
  TenantBillingDto getTenantBillingForCurrentTenant();

  void createTenantBilling(@Nonnull TenantBillingDto dto);

  @Nonnull
  String createSubscriptionUrlForCheckout(@Nonnull Long userId);

  @Nonnull
  String getSubscriptionOverviewUrl();

  boolean hasValidSubscriptionByTenantId(@Nonnull Long tenantId);

}
