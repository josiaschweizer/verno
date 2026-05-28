package ch.verno.server.service.extern.tenant;

import ch.verno.common.api.dto.exernal.tenant.CreateTenantRequest;
import ch.verno.common.api.dto.exernal.tenant.CreateTenantResponse;
import ch.verno.common.db.dto.table.AppUserSettingDto;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.db.service.intern.IAppUserSettingService;
import ch.verno.common.db.service.intern.IGenderService;
import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import ch.verno.common.exceptions.server.service.TenantProvisionFailedException;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.gender.Gender;
import ch.verno.common.lib.gender.GenderConstants;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.lib.language.Language;
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

import java.util.List;

@Service
public class TenantProvisionService {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final TenantRepository tenantRepository;
  @Nonnull private final AppUserRepository appUserRepository;

  @Nonnull private final IGenderService genderService;
  @Nonnull private final IAppUserSettingService appUserSettingService;

  @PersistenceContext
  private EntityManager em;

  public TenantProvisionService(@Nonnull final GlobalInterface globalInterface) {
    this.passwordEncoder = globalInterface.getPasswordEncoder();
    this.tenantRepository = globalInterface.getService(TenantRepository.class);
    this.appUserRepository = globalInterface.getService(AppUserRepository.class);

    this.genderService = globalInterface.getService(IGenderService.class);
    this.appUserSettingService = globalInterface.getService(IAppUserSettingService.class);
  }

  @Transactional(rollbackFor = Exception.class)
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest request) {
    if (tenantRepository.existsBySlug(request.tenantKey())) {
      throw new TenantAlreadyExistsException("tenantKey already exists: " + request.tenantKey());
    }

    final var newTenantId = tenantRepository.nextId();
    final var savedTenant = saveTenant(request, newTenantId);

    final var newUser = saveUser(request, savedTenant);
    saveAppUserSetting(request, newUser);

    saveNewGenders(request.preferredLanguage());

    return new CreateTenantResponse(newTenantId, request.tenantKey(), request.subdomain(), false, VernoConstants.STATUS_CREATED);
  }

  @Nonnull
  private TenantEntity saveTenant(@Nonnull final CreateTenantRequest request,
                                  @Nonnull final Long newTenantId) {
    final var tenant = TenantEntity.ref(newTenantId);
    tenant.setSlug(request.tenantKey());
    tenant.setName(request.tenantName());
    final var save = tenantRepository.save(tenant);

    //set tenant context for further db transactions
    TenantContext.set(save.getId());

    return save;
  }

  @Nonnull
  private AppUserEntity saveUser(@Nonnull final CreateTenantRequest request,
                                 @Nonnull final TenantEntity savedTenant) {
    final var tenantRef = em.getReference(TenantEntity.class, savedTenant.getId());
    final var pwHash = passwordEncoder.encode(request.adminPassword());

    final var admin = new AppUserEntity(
            tenantRef,
            request.adminUsername(),
            request.adminFirstname(),
            request.adminLastname(),
            request.adminEmail(),
            pwHash != null ? pwHash : Publ.EMPTY_STRING,
            VernoConstants.ADMIN_ROLE,
            true
    );

    try {
      final var newUser = appUserRepository.save(admin);
      appUserRepository.flush();

      return newUser;
    } catch (Exception e) {
      throw new TenantProvisionFailedException("Failed to create admin user for tenant: " + request.tenantKey(), e);
    }
  }

  private void saveAppUserSetting(@Nonnull final CreateTenantRequest request,
                                  @Nonnull final AppUserEntity newUser) {
    final var dto = new AppUserSettingDto();
    dto.setUserId(newUser.getId());
    dto.setTheme(null);
    dto.setLanguageTag(request.preferredLanguage());

    appUserSettingService.saveAppUserSetting(dto);
  }

  private void saveNewGenders(@Nonnull String preferredLanguage) {
    final var userLanguage = Language.of(preferredLanguage);
    final var genders = getGenders(userLanguage);

    for (final var gender : genders) {
      genderService.createGender(gender);
    }
  }

  private List<GenderDto> getGenders(@Nonnull Language userLanguage) {
    final var male = GenderDto.empty();
    male.setName(GenderConstants.INTERNAL_MALE);
    male.setDescription(GenderUtil.getDescriptionFromLanguage(Gender.MALE, userLanguage));

    final var female = GenderDto.empty();
    female.setName(GenderConstants.INTERNAL_FEMALE);
    female.setDescription(GenderUtil.getDescriptionFromLanguage(Gender.FEMALE, userLanguage));

    return List.of(male, female);
  }

  public long getCountOfTenants() {
    return tenantRepository.count();
  }
}