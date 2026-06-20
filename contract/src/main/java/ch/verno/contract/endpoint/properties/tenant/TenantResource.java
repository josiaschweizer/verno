package ch.verno.contract.endpoint.properties.tenant;

import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.Optional;

@RpcEndpoint
public interface TenantResource {

  @Nonnull
  Optional<TenantDto> resolveTenant();

}
