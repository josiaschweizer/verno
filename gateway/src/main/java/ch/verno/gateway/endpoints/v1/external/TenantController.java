package ch.verno.gateway.endpoints.v1.external;

import ch.verno.contract.endpoint.tenant.TenantProvisionResource;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.gateway.base.BaseController;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TENANTS)
public class TenantController extends BaseController {

  @Nonnull private final TenantProvisionResource tenantProvisionResource;

  public TenantController(@Nonnull final RpcFactory rpcFactory) {
    this.tenantProvisionResource = rpcFactory.create(TenantProvisionResource.class);
  }

  @Nonnull
  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateTenantRequest req) {
    return created(tenantProvisionResource.createTenant(req));
  }

  @Nonnull
  @GetMapping(ApiUrl.COUNT)
  public ResponseEntity<?> getCountOfTenants() {
    return ok(tenantProvisionResource.getCountOfTenants());
  }
}