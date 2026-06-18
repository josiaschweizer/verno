package ch.verno.gateway.external.controller.tenant;

import ch.verno.contract.gateway.ApiUrl;
import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.gateway.base.BaseController;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiUrl.TENANTS)
public class TenantController extends BaseController {

  private final TenantProvisionService tenantProvisionService;

  public TenantController(@Nonnull final GlobalInterface globalInterface) {
    this.tenantProvisionService = globalInterface.getService(TenantProvisionService.class);
  }

  @Nonnull
  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateTenantRequest req) {
    return created(tenantProvisionService.createTenant(req));
  }

  @Nonnull
  @GetMapping(ApiUrl.COUNT)
  public ResponseEntity<?> getCountOfTenants() {
    return ok(tenantProvisionService.getCountOfTenants());
  }
}