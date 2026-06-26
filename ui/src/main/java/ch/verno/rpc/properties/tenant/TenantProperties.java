package ch.verno.rpc.properties.tenant;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.contract.endpoint.properties.tenant.TenantResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class TenantProperties {

  @Nonnull private final Lazy<TenantResource> tenantResource;

  @Inject
  public TenantProperties(@Nonnull final RpcFactory rpcFactory) {
    this.tenantResource = Lazy.of(() -> rpcFactory.create(TenantResource.class));
  }

  @Nonnull
  public Optional<TenantDto> resolveCurrentTenant() {
    return tenantResource.get().resolveTenant();
  }

  @Nonnull
  public List<TenantDto> findAllTenants() {
    return tenantResource.get().findAllTenants();
  }

}
