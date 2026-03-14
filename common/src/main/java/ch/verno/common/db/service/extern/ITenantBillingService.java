package ch.verno.common.db.service.extern;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import jakarta.annotation.Nonnull;

import java.util.List;

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
  List<TenantBillingDto> getTenantBillings();

  @Nonnull
  TenantBillingDto saveTenantBilling(@Nonnull TenantBillingDto dto);
}
