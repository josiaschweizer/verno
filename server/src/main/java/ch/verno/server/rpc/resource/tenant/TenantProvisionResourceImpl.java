package ch.verno.server.rpc.resource.tenant;

import ch.verno.contract.endpoint.tenant.TenantProvisionResource;
import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.contract.response.tenant.create.CreateTenantResponse;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.tenant.TenantProvisionBo;
import ch.verno.server.service.entity.tenant.TenantService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(TenantProvisionResource.class)
public class TenantProvisionResourceImpl implements TenantProvisionResource {

  @Nonnull private final Lazy<TenantService> tenantService;
  @Nonnull private final Lazy<TenantProvisionBo> tenantProvisionBo;

  public TenantProvisionResourceImpl(@Nonnull final ServerBean serverBean) {
    this.tenantService = Lazy.of(() -> serverBean.get(TenantService.class));
    this.tenantProvisionBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(TenantProvisionBo.class));
  }

  @Nonnull
  @Override
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest request) {
    return tenantProvisionBo.get().createTenant(request);
  }

  @Override
  public long getCountOfTenants() {
    return tenantService.get().countTenants();
  }
}
