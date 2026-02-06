package ch.verno.server.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.common.lib.Routes;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantFilters;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

  private static final String ATTR_PUBLIC_NO_TENANT = "PUBLIC_NO_TENANT";

  @Nonnull private final TenantProperties props;
  @Nonnull private final TenantResolver resolver;
  @Nonnull private final EntityManager entityManager;

  public TenantFilter(@Nonnull final TenantProperties props,
                      @Nonnull final TenantResolver resolver,
                      @Nonnull final EntityManager entityManager) {
    this.props = props;
    this.resolver = resolver;
    this.entityManager = entityManager;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
    if (!props.isEnabled()) {
      return true;
    }

    String path = request.getRequestURI();
    final var context = request.getContextPath();
    if (context != null && !context.isEmpty() && path.startsWith(context)) {
      path = path.substring(context.length());
    }

    final var session = request.getSession(false);
    if (session != null && Boolean.TRUE.equals(session.getAttribute(ATTR_PUBLIC_NO_TENANT))) {
      return true;
    }

    if (path.startsWith(ApiUrl.BASE_API)) {
      return true;
    }

    switch (path) {
      case Publ.SLASH + Routes.TENANT_NOT_FOUND, Publ.SLASH + Routes.TENANT_NOT_FOUND + Publ.SLASH -> {
        request.getSession(true).setAttribute(ATTR_PUBLIC_NO_TENANT, Boolean.TRUE);
        return true;
      }
      case "/UIDL", "/HEARTBEAT", "/PUSH" -> {
        return true;
      }
    }

    return path.startsWith("/VAADIN/")
            || path.startsWith("/frontend/")
            || path.startsWith("/assets/")
            || path.equals("/favicon.ico");
  }

  @Override
  protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                  @Nonnull HttpServletResponse response,
                                  @Nonnull FilterChain filterChain) throws ServletException, IOException {
    if (!props.isEnabled()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      final Long tenantId = resolver.resolveTenantId(request).orElseThrow(() -> new TenantNotResolvedException("Tenant could not be resolved"));

      final var httpSession = request.getSession(false);
      if (httpSession != null) {
        httpSession.removeAttribute(ATTR_PUBLIC_NO_TENANT);
      }

      TenantContext.set(tenantId);

      final var session = entityManager.unwrap(Session.class);
      session.enableFilter(TenantFilters.TENANT_FILTER).setParameter(TenantFilters.PARAM_TENANT_ID, tenantId);

      filterChain.doFilter(request, response);
    } catch (TenantNotResolvedException e) {
      response.sendRedirect(request.getContextPath() + Routes.TENANT_NOT_FOUND);
    } finally {
      TenantContext.clear();
    }
  }
}