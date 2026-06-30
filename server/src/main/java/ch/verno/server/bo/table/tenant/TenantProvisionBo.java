package ch.verno.server.bo.table.tenant;

import ch.verno.common.db.role.Role;
import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import ch.verno.common.lib.gender.Gender;
import ch.verno.common.lib.gender.GenderConstants;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.request.tenant.create.CreateTenantRequest;
import ch.verno.contract.response.tenant.create.CreateTenantResponse;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.gender.GenderService;
import ch.verno.server.service.entity.setting.AppUserSettingService;
import ch.verno.server.service.entity.user.AppUserService;
import ch.verno.server.service.entity.tenant.TenantService;
import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

public class TenantProvisionBo {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final Lazy<TenantService> tenantService;
  @Nonnull private final Lazy<GenderService> genderService;
  @Nonnull private final Lazy<AppUserService> appUserService;
  @Nonnull private final Lazy<AppUserSettingService> appUserSettingService;

  protected TenantProvisionBo(@Nonnull final ServerBean serverBean) {
    this.passwordEncoder = serverBean.get(PasswordEncoder.class);
    this.tenantService = Lazy.of(() -> serverBean.get(TenantService.class));
    this.genderService = Lazy.of(() -> serverBean.get(GenderService.class));
    this.appUserService = Lazy.of(() -> serverBean.get(AppUserService.class));
    this.appUserSettingService = Lazy.of(() -> serverBean.get(AppUserSettingService.class));
  }

  @Nonnull
  public CreateTenantResponse createTenant(@Nonnull final CreateTenantRequest request) {
    if (tenantService.get().existsBySlug(request.tenantKey())) {
      throw new TenantAlreadyExistsException("tenantKey already exists: " + request.tenantKey());
    }

    final var savedTenant = saveTenant(request);
    TenantContext.set(savedTenant.id());

    final var savedUser = saveUser(request);
    saveUserSetting(savedUser, request.preferredLanguage());
    saveGenders(request.preferredLanguage());

    return new CreateTenantResponse(
            savedTenant.id(),
            savedTenant.slug(),
            request.subdomain(),
            false,
            VernoConstants.STATUS_CREATED
    );
  }

  @Nonnull
  private TenantDto saveTenant(@Nonnull final CreateTenantRequest request) {
    final var newTenantId = tenantService.get().nextId();
    final var newTenantDto = fromTenantRequest(request, newTenantId);
    return tenantService.get().create(newTenantDto);
  }

  @Nonnull
  private AppUserDto saveUser(@Nonnull final CreateTenantRequest request) {
    final var passwordHash = passwordEncoder.encode(request.adminPassword());
    final var admin = new AppUserDto(
            request.adminUsername(),
            request.adminFirstname(),
            request.adminLastname(),
            request.adminEmail(),
            Optional.ofNullable(passwordHash).orElse(Publ.EMPTY_STRING),
            Role.ADMIN,
            true
    );

    return appUserService.get().save(admin);
  }

  private void saveUserSetting(@Nonnull final AppUserDto userDto,
                               @Nonnull final String preferredLanguage) {
    final var defaultUserSetting = new AppUserSettingDto(
            userDto.getId(),
            null,
            Language.of(preferredLanguage)
    );

    appUserSettingService.get().save(defaultUserSetting);
  }

  private void saveGenders(@Nonnull String preferredLanguage) {
    final var userLanguage = Language.of(preferredLanguage);
    final var genders = getDefaultGenders(userLanguage);

    for (final var gender : genders) {
      genderService.get().save(gender);
    }
  }

  @Nonnull
  private List<GenderDto> getDefaultGenders(@Nonnull Language userLanguage) {
    final var male = GenderDto.empty();
    male.setName(GenderConstants.INTERNAL_MALE);
    male.setDescription(GenderUtil.getDescriptionFromLanguage(Gender.MALE, userLanguage));

    final var female = GenderDto.empty();
    female.setName(GenderConstants.INTERNAL_FEMALE);
    female.setDescription(GenderUtil.getDescriptionFromLanguage(Gender.FEMALE, userLanguage));

    return List.of(male, female);
  }

  @Nonnull
  private TenantDto fromTenantRequest(@Nonnull final CreateTenantRequest request,
                                      @Nonnull final Long newTenantId) {
    return new TenantDto(
            newTenantId,
            request.tenantKey(),
            request.tenantName()
    );
  }

}
