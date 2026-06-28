package ch.verno.ui.verno.security;

import ch.verno.common.lib.Routes;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.properties.tenant.TenantProperties;
import ch.verno.rpc.properties.user.UserProperties;
import com.google.inject.Inject;
import com.google.inject.Injector;
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

  @Nonnull private final Lazy<UserProperties> userProperties;
  @Nonnull private final Lazy<TenantProperties> tenantProperties;
  @Nonnull private final AuthenticationContext authenticationContext;

  @Inject
  public AutoLoginConfig(@Nonnull final Injector injector,
                         @Nonnull final AuthenticationContext authenticationContext) {
    this.userProperties = Lazy.of(() -> injector.getInstance(UserProperties.class));
    this.tenantProperties = Lazy.of(() -> injector.getInstance(TenantProperties.class));
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

    final var userDetailsOptional = userProperties.get().findOptionalByUsernameOrEmail(devUser);
    if (userDetailsOptional.isEmpty()) {
      return;
    }

    final var userDetails = userDetailsOptional.get();
    final var authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetailsOptional.get().getAuthorities()
    );

    final var securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
    session.setAttribute(VernoConstants.SPRING_SECURITY_CONTEXT, securityContext);

    event.getUI().getPage().reload();
  }

  private void applyTenant() {
    final var tenants = tenantProperties.get().findAllTenants();
    if (tenants.isEmpty()) {
      return;
    }

    tenants.forEach(tenant -> {
      if (tenant.id() == 7777L) {
        TenantContext.set(tenant.id());
      }
    });

    if (TenantContext.get() == null) {
      TenantContext.set(tenants.getFirst().id());
    }
  }
}
