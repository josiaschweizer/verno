package ch.verno.api.endpoints.external;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.exernal.CreateTenantRequest;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.TenantProvisionService;
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