package ch.verno.rpc.properties.tenant;

import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.contract.endpoint.properties.tenant.TenantResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class TenantProperties {

  @Nonnull private final Lazy<TenantResource> tenantResource;

  public TenantProperties(@Nonnull final RpcFactory rpcFactory) {
    this.tenantResource = Lazy.of(() -> rpcFactory.create(TenantResource.class));
  }

  @Nonnull
  public Optional<TenantDto> resolveCurrentTenant() {
    return tenantResource.get().resolveTenant();
  }

}
