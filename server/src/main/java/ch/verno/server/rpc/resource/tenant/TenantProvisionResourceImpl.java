package ch.verno.server.rpc.resource.tenant;

import ch.verno.contract.endpoint.tenant.TenantProvisionResource;
import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.contract.response.tenant.create.CreateTenantResponse;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
@RpcResource(TenantProvisionResource.class)
public class TenantProvisionResourceImpl implements TenantProvisionResource {

  public TenantProvisionResourceImpl(@Nonnull final ServerBean serverBean) {

  }

  @Nonnull
  @Override
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest request) {
    return null;
  }

  @Override
  public long getCountOfTenants() {
    return 0;
  }
}
