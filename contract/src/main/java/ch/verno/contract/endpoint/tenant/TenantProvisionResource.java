package ch.verno.contract.endpoint.tenant;

import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.contract.response.tenant.create.CreateTenantResponse;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface TenantProvisionResource {

  /**
   * Creates a tenant including its initial administrator and default data.
   *
   * @param request tenant provisioning request
   * @return result of the tenant provisioning
   */
  @Nonnull
  CreateTenantResponse createTenant(@Nonnull CreateTenantRequest request);

  /**
   * Returns the total number of tenants.
   *
   * @return number of tenants
   */
  long getCountOfTenants();

}
