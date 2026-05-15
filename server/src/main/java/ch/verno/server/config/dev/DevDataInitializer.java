package ch.verno.server.config.dev;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.publ.VernoConstants;
import ch.verno.server.repository.AppUserRepository;
import ch.verno.server.repository.TenantRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@Profile("dev")
public class DevDataInitializer implements ApplicationRunner {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final TenantRepository tenantRepository;
  @Nonnull private final AppUserRepository appUserRepository;

  public DevDataInitializer(@Nonnull final GlobalInterface globalInterface) {
    this.passwordEncoder = globalInterface.getPasswordEncoder();
    this.tenantRepository = globalInterface.getService(TenantRepository.class);
    this.appUserRepository = globalInterface.getService(AppUserRepository.class);
  }

  @Override
  public void run(@Nonnull final ApplicationArguments arguments) throws Exception {
    final var tenantId = 7777L;

    final var tenant = tenantRepository.findById(tenantId)
            .orElseGet(() -> {
              final var newTenant = TenantEntity.ref(tenantId);
              newTenant.setSlug("verno");
              newTenant.setName("Verno Dev");
              return tenantRepository.save(newTenant);
            });

    TenantContext.set(tenant.getId());
    final var adminExists = appUserRepository.existsByUsernameAndTenantId("admin", tenant.getId());
    if (adminExists) {
      return;
    }

    final var admin = new AppUserEntity(
            tenant,
            "admin",
            "Admin",
            "User",
            "admin@verno.local",
            passwordEncoder.encode("admin1234"),
            VernoConstants.ADMIN_ROLE,
            true
    );

    appUserRepository.save(admin);
  }

}
