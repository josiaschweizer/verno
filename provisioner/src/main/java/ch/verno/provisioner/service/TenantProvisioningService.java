package ch.verno.provisioner.service;

import ch.verno.provisioner.api.dto.CreateTenantRequest;
import ch.verno.provisioner.db.entity.TenantRecord;
import ch.verno.provisioner.db.repository.TenantRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TenantProvisioningService {

  @Nonnull private final TenantRepository repo;
  @Nonnull private final PasswordEncoder passwordEncoder;

  public TenantProvisioningService(@Nonnull final TenantRepository repo,
                                   @Nonnull final PasswordEncoder passwordEncoder) {
    this.repo = repo;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(readOnly = true)
  public List<TenantRecord> listTenants() {
    return repo.findAll();
  }

  @Transactional
  public TenantRecord createAndProvisionTenant(@Nonnull final CreateTenantRequest req) {
    final UUID id;

    final var tenantRecords = listTenants();
    for (final var tenantRecord : tenantRecords) {
      System.out.println(tenantRecord);
    }

    try {
      id = repo.insertTenant(req.tenantKey(), req.tenantName(), req.subdomain(), req.preferredLanguage());
    } catch (DuplicateKeyException e) {
      final var existing = repo.findByTenantKey(req.tenantKey()).orElseThrow(() -> new IllegalStateException("Duplicate tenantKey but cannot load tenant", e));

      if (!"provisioned".equalsIgnoreCase(existing.dbStatus())) {
        final var pwHash = passwordEncoder.encode(req.adminPassword());
        repo.callProvisionFunction(req.tenantKey(), req.adminEmail(), req.adminDisplayName(), pwHash);
      }
      return repo.findByTenantKey(req.tenantKey()).orElseThrow();
    }

    final var pwHash = passwordEncoder.encode(req.adminPassword());
    repo.callProvisionFunction(req.tenantKey(), req.adminEmail(), req.adminDisplayName(), pwHash);

    return repo.findByTenantKey(req.tenantKey())
            .orElseThrow(() -> new IllegalStateException("Tenant not found after provisioning: " + id));
  }

  public Optional<TenantRecord> getTenant(final String tenantKey) {
    return repo.findByTenantKey(tenantKey);
  }

  @Transactional
  public TenantRecord reconcileTenant(final String tenantKey) {
    final var existing = repo.findByTenantKey(tenantKey)
            .orElseThrow(() -> new IllegalArgumentException("tenantKey not found: " + tenantKey));

    if (!"provisioned".equalsIgnoreCase(existing.dbStatus())) {
      repo.callProvisionFunction(tenantKey, null, null, null);
    }
    return repo.findByTenantKey(tenantKey).orElseThrow();
  }
}