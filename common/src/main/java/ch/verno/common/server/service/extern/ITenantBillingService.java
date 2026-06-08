package ch.verno.common.server.service.extern;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ITenantBillingService {

  @Nonnull
  TenantBillingDto updateTenantBilling(@Nonnull TenantBillingDto dto);

  @Nonnull
  TenantBillingDto createTenantBilling(@Nonnull TenantBillingDto dto);

  @Nonnull
  TenantBillingDto getTenantBillingById(@Nonnull Long id);

  @Nonnull
  TenantBillingDto getTenantBillingByTenantId(@Nonnull Long tenantId);

  @Nonnull
  Optional<TenantBillingDto> getOptionalTenantBillingByTenantId(@Nonnull Long tenantId);

  @Nonnull
  Optional<TenantBillingDto> getOptionalTenantBillingByStripeCustomerId(@Nonnull String stripeCustomerId);

  @Nonnull
  List<TenantBillingDto> getTenantBillings();

  @Nonnull
  TenantBillingDto saveTenantBilling(@Nonnull TenantBillingDto dto);

  boolean hasTenantValidSubscription(@Nonnull Long tenantId);

}
