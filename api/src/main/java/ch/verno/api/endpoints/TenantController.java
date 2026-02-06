package ch.verno.api.endpoints;

import ch.verno.api.base.BaseController;
import ch.verno.common.api.dto.CreateTenantRequest;
import ch.verno.server.service.extern.TenantProvisionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController extends BaseController {

  private final TenantProvisionService tenantProvisionService;

  public TenantController(TenantProvisionService tenantProvisionService) {
    this.tenantProvisionService = tenantProvisionService;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody CreateTenantRequest req) {
    return created(tenantProvisionService.createTenant(req));
  }
}