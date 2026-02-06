package ch.verno.server.service.extern;

import ch.verno.common.api.dto.CreateTenantRequest;
import ch.verno.common.api.dto.CreateTenantResponse;
import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
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

  @Transactional
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest req) {
    if (tenantRepository.existsBySlug(req.tenantKey())) {
      throw new TenantAlreadyExistsException("tenantKey already exists: " + req.tenantKey());
    }

    final Long newId = tenantRepository.nextId(); //TODO tenant id von user setzten lassen?

    final TenantEntity m = TenantEntity.ref(newId);
    m.setSlug(req.tenantKey());
    m.setName(req.tenantName());
    tenantRepository.save(m);

    final var tenantRef = em.getReference(TenantEntity.class, newId);
    final var username = req.adminEmail();


    final var hash = passwordEncoder.encode(req.adminPassword());

    final AppUserEntity admin = new AppUserEntity(
            tenantRef,
            username,
            hash != null ? hash : Publ.EMPTY_STRING,
            VernoConstants.ADMIN_ROLE
    );
    appUserRepository.save(admin);

    return new CreateTenantResponse(newId, req.tenantKey(), req.subdomain(), VernoConstants.STATUS_CREATED);
  }
}