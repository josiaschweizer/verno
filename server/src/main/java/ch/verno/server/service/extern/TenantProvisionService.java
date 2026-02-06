package ch.verno.server.service.extern;

import ch.verno.common.api.dto.CreateTenantRequest;
import ch.verno.common.api.dto.CreateTenantResponse;
import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import ch.verno.server.repository.AppUserRepository;
import ch.verno.server.repository.MandantRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TenantProvisionService {

  private final MandantRepository mandantRepository;
  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;

  @PersistenceContext
  private EntityManager em;

  public TenantProvisionService(@Nonnull final MandantRepository mandantRepository,
                                @Nonnull final AppUserRepository appUserRepository,
                                @Nonnull final PasswordEncoder passwordEncoder) {
    this.mandantRepository = mandantRepository;
    this.appUserRepository = appUserRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest req) {
    if (mandantRepository.existsBySlug(req.tenantKey())) {
      throw new TenantAlreadyExistsException("tenantKey already exists: " + req.tenantKey());
    }

    final Long newId = mandantRepository.nextId(); //TODO mandant id von user setzten lassen?

    final MandantEntity m = MandantEntity.ref(newId);
    m.setSlug(req.tenantKey());
    m.setName(req.tenantName());
    mandantRepository.save(m);

    final var mandantRef = em.getReference(MandantEntity.class, newId);
    final var username = req.adminEmail();


    final var hash = passwordEncoder.encode(req.adminPassword());

    final AppUserEntity admin = new AppUserEntity(
            mandantRef,
            username,
            hash != null ? hash : Publ.EMPTY_STRING,
            VernoConstants.ADMIN_ROLE
    );
    appUserRepository.save(admin);

    return new CreateTenantResponse(newId, req.tenantKey(), req.subdomain(), VernoConstants.STATUS_CREATED);
  }
}