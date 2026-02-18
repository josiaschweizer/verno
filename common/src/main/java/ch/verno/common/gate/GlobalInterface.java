package ch.verno.common.gate;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.dto.table.TenantDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.db.service.ITenantService;
import ch.verno.common.tenant.TenantContext;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Optional;

@Service
public class GlobalInterface {

  private static ApplicationContext context;

  @Autowired
  public GlobalInterface(@Nonnull final ApplicationContext applicationContext) {
    context = applicationContext;
  }

  @Nonnull
  public static GlobalInterface getInstance() {
    return context.getBean(GlobalInterface.class);
  }

  @Nonnull
  public <T> T getGate(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public <T> T getService(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public PasswordEncoder getPasswordEncoder() {
    return context.getBean(PasswordEncoder.class);
  }

  /**
   *
   * @return the current user, or empty if no user is authenticated
   */
  @Nonnull
  public Optional<AppUserDto> getOptionalCurrentUser() {
    final var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
    if (authentication == null) {
      return Optional.empty();
    }

    final var appUserService = getService(IAppUserService.class);
    return appUserService.findByUserName(authentication
            .getName());
  }

  /**
   *
   * @return the current user, or throws an exception if no user is authenticated
   * @throws IllegalStateException if no user is authenticated
   */
  @Nonnull
  public AppUserDto getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
    if (authentication == null) {
      throw new IllegalStateException("No user is authenticated");
    }

    final var appUserService = getService(IAppUserService.class);
    final var found = appUserService.findByUserName(authentication.getName());
    if (found.isEmpty()) {
      throw new IllegalStateException("Authenticated user not found in database: " + authentication.getName());
    }

    return found.get();
  }

  public void logout() {
    final var ui = UI.getCurrent();

    SecurityContextHolder.clearContext();
    ui.getSession().getSession().invalidate();
    ui.getPage().setLocation(Routes.LOGIN);
  }

  @Nonnull
  public Locale getLocale() {
    return UI.getCurrent().getLocale();
  }

  @Nonnull
  public I18NProvider getI18NProvider() {
    return context.getBean(I18NProvider.class);
  }

  @Nullable
  public TenantDto resolveTenant() {
    final var tenantService = getService(ITenantService.class);
    final var tenantId = TenantContext.get();

    if (tenantId != null) {
      return tenantService.findById(tenantId).orElse(null);
    }

    return null;
  }
}
