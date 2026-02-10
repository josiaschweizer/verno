package ch.verno.server.service.extern;

import ch.verno.common.api.dto.CreateTenantRequest;
import ch.verno.common.api.dto.CreateTenantResponse;
import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import ch.verno.common.exceptions.server.service.TenantProvisionFailedException;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import ch.verno.server.repository.AppUserRepository;
import ch.verno.server.repository.TenantRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantProvisionService {

  private final TenantRepository tenantRepository;
  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

  @PersistenceContext
  private EntityManager em;

  public TenantProvisionService(@Nonnull final TenantRepository tenantRepository,
                                @Nonnull final AppUserRepository appUserRepository,
                                @Nonnull final PasswordEncoder passwordEncoder) {
    this.tenantRepository = tenantRepository;
    this.appUserRepository = appUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional(rollbackFor = Exception.class)
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest req) {
    if (tenantRepository.existsBySlug(req.tenantKey())) {
      throw new TenantAlreadyExistsException("tenantKey already exists: " + req.tenantKey());
    }

    final Long newId = tenantRepository.nextId();

    final var tenant = TenantEntity.ref(newId);
    tenant.setSlug(req.tenantKey());
    tenant.setName(req.tenantName());
    final var save = tenantRepository.save(tenant);
    TenantContext.set(save.getId());

    final var tenantRef = em.getReference(TenantEntity.class, save.getId());
    final var username = req.adminUsername();

    final var hash = passwordEncoder.encode(req.adminPassword());

    final var admin = new AppUserEntity(
            tenantRef,
            username,
            req.adminFirstname(),
            req.adminLastname(),
            req.adminEmail(),
            hash != null ? hash : Publ.EMPTY_STRING,
            VernoConstants.ADMIN_ROLE,
            true
    );

    try {
      appUserRepository.save(admin);
      appUserRepository.flush();
    } catch (Exception e) {
      throw new TenantProvisionFailedException("Failed to create admin user for tenant: " + req.tenantKey(), e);
    }

    return new CreateTenantResponse(newId, req.tenantKey(), req.subdomain(), false, VernoConstants.STATUS_CREATED);
  }
}