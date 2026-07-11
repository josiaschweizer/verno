package ch.verno.server.bo.table.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.tenant.TenantService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class TenantBo {

  @Nonnull private final Lazy<TenantService> tenantService;

  protected TenantBo(@Nonnull final ServerBean serverBean) {
    this.tenantService = Lazy.of(() -> serverBean.get(TenantService.class));
  }

  @Nonnull
  public Optional<TenantDto> getOptionalCurrentTenant() {
    final var optionalId = getOptionalCurrentTenantId();
    return optionalId.flatMap(id -> tenantService.get().findById(id));
  }

  @Nonnull
  public TenantDto getCurrentTenant() {
    final var optionalTenant = getOptionalCurrentTenant();
    if (optionalTenant.isPresent()) {
      return optionalTenant.get();
    } else {
      throw new TenantNotResolvedException("No current tenant found");
    }
  }

  @Nonnull
  private Optional<Long> getOptionalCurrentTenantId() {
    return Optional.ofNullable(TenantContext.get());
  }


}
