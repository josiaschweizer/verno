package ch.verno.ui.verno.security;

import ch.verno.publ.Routes;
import ch.verno.common.tenant.TenantContext;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import ch.verno.server.service.intern.AppUserService;
import ch.verno.server.tenant.TenantLookupService;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty("verno.dev.user")
public class AutoLoginConfig implements VaadinServiceInitListener {

  @Value("${verno.dev.user}")
  private String devUser;

  @Nonnull private final AppUserService appUserService;
  @Nonnull private final TenantLookupService tenantLookupService;
  @Nonnull private final AuthenticationContext authenticationContext;

  public AutoLoginConfig(@Nonnull final AppUserService appUserService,
                         @Nonnull final TenantLookupService tenantLookupService,
                         @Nonnull final AuthenticationContext authenticationContext) {
    this.appUserService = appUserService;
    this.tenantLookupService = tenantLookupService;
    this.authenticationContext = authenticationContext;
  }

  @Override
  public void serviceInit(@Nonnull final ServiceInitEvent event) {
    event.getSource().addUIInitListener(uiEvent -> uiEvent.getUI().addBeforeEnterListener(this::performAutoLogin));
  }

  private void performAutoLogin(@Nonnull final BeforeEnterEvent event) {
    if (!event.getLocation().getPath().equals(Routes.LOGIN) || authenticationContext.isAuthenticated()) {
      return;
    }

    final var session = event.getUI().getSession().getSession();
    if (session.getAttribute(VernoConstants.AUTO_LOGIN_ATTEMPTED) != null) {
      return;
    }
    session.setAttribute(VernoConstants.AUTO_LOGIN_ATTEMPTED, true);

    applyTenant();

    final var userDetails = appUserService.loadUserByUsername(devUser);
    final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    final var securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    session.setAttribute(VernoConstants.SPRING_SECURITY_CONTEXT, securityContext);


    event.getUI().getPage().reload();
  }

  private void applyTenant() {
    final var tenants = tenantLookupService.findAllTenants();
    if (tenants.isEmpty()) {
      return;
    }

    tenants.forEach(tenant -> {
      if (tenant.getId() == 7777L) {
        TenantContext.set(tenant.getId());
      }
    });

    if (TenantContext.get() == null) {
      TenantContext.set(tenants.getFirst().getId());
    }
  }
}
