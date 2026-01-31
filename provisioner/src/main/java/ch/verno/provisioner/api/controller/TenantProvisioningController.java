package ch.verno.provisioner.api.controller;

import ch.verno.provisioner.api.dto.CreateTenantRequest;
import ch.verno.provisioner.api.dto.CreateTenantResponse;
import ch.verno.provisioner.api.dto.TenantStatusResponse;
import ch.verno.provisioner.service.TenantProvisioningService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantProvisioningController {

  private final TenantProvisioningService service;

  public TenantProvisioningController(final TenantProvisioningService service) {
    this.service = service;
  }

  @GetMapping
  public List<TenantStatusResponse> listTenants() {
    return service.listTenants().stream()
            .map(t -> new TenantStatusResponse(
                    t.id(),
                    t.tenantKey(),
                    t.tenantName(),
                    t.tenantSubdomain(),
                    t.preferredLanguage(),
                    t.status(),
                    t.schemaName(),
                    t.dbStatus(),
                    t.provisionedAt(),
                    t.createdAt(),
                    t.updatedAt(),
                    null,
                    null
            ))
            .toList();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CreateTenantResponse createTenant(@Valid @RequestBody CreateTenantRequest req) {
    final var tenant = service.createAndProvisionTenant(req);

    return new CreateTenantResponse(
            tenant.id(),
            tenant.tenantKey(),
            tenant.schemaName(),
            tenant.status(),
            tenant.dbStatus(),
            tenant.provisionedAt(),
            tenant.createdAt(),
            tenant.updatedAt()
    );
  }

  @GetMapping("/{tenantKey}")
  public TenantStatusResponse getTenant(@PathVariable String tenantKey) {
    final var t = service.getTenant(tenantKey)
            .orElseThrow(() -> new IllegalArgumentException("tenantKey not found: " + tenantKey));
    return new TenantStatusResponse(
            t.id(),
            t.tenantKey(),
            t.tenantName(),
            t.tenantSubdomain(),
            t.preferredLanguage(),
            t.status(),
            t.schemaName(),
            t.dbStatus(),
            t.provisionedAt(),
            t.createdAt(),
            t.updatedAt(),
            null,
            null
    );
  }

  @PostMapping("/{tenantKey}/reconcile")
  public TenantStatusResponse reconcile(@PathVariable String tenantKey) {
    final var t = service.reconcileTenant(tenantKey);
    return new TenantStatusResponse(
            t.id(),
            t.tenantKey(),
            t.tenantName(),
            t.tenantSubdomain(),
            t.preferredLanguage(),
            t.status(),
            t.schemaName(),
            t.dbStatus(),
            t.provisionedAt(),
            t.createdAt(),
            t.updatedAt(),
            null,
            null
    );
  }
}