package ch.verno.server.rpc.properties.tenant;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.contract.endpoint.properties.tenant.TenantResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.tenant.TenantService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RpcResource(TenantResource.class)
public class TenantResourceImpl implements TenantResource {

  @Nonnull private final Lazy<TenantService> tenantService;

  public TenantResourceImpl(@Nonnull final ServerBean serverBean) {
    this.tenantService = Lazy.of(() -> serverBean.get(TenantService.class));
  }

  @Nonnull
  @Override
  public Optional<TenantDto> resolveTenant() {
    final var currentTenant = TenantContext.get();
    if (currentTenant == null) {
      return Optional.empty();
    }

    return tenantService.get().findById(currentTenant);
  }

  @Nonnull
  @Override
  public List<TenantDto> findAllTenants() {
    return tenantService.get().findAllTenants();
  }
}
