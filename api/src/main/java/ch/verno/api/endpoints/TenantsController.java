package ch.verno.api.endpoints;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.CreateTenantRequest;
import ch.verno.common.api.dto.CreateTenantResponse;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.TenantProvisionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantsController extends BaseController {

  private final TenantProvisionService tenantProvisionService;

  public TenantsController(TenantProvisionService tenantProvisionService) {
    this.tenantProvisionService = tenantProvisionService;
  }

  @PostMapping
  public ResponseEntity<?> create(
          @RequestHeader("X-Tenant-Key") String tenantKey,
          @RequestBody CreateTenantRequest req
  ) {
    return created(tenantProvisionService.createTenant(req));
  }
}