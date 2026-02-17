package ch.verno.server.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.publ.Routes;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantFilters;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

public class TenantFilter extends OncePerRequestFilter {

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
    if (session != null && Boolean.TRUE.equals(session.getAttribute(VernoConstants.ATTR_PUBLIC_NO_TENANT))) {
      return true;
    }

    if (path.startsWith(ApiUrl.BASE_API)) {
      return true;
    }

    switch (path) {
      case Publ.SLASH + Routes.TENANT_NOT_FOUND, Publ.SLASH + Routes.TENANT_NOT_FOUND + Publ.SLASH -> {
        request.getSession(true).setAttribute(VernoConstants.ATTR_PUBLIC_NO_TENANT, Boolean.TRUE);
        return true;
      }
      case "/UIDL", "/HEARTBEAT", "/PUSH" -> {
        return true;
      }
    }

    return path.startsWith("/VAADIN/")
            || path.startsWith("/frontend/")
            || path.startsWith("/assets/")
            || path.startsWith("/icons/")
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
      final var tenantId = resolveTenantIdWithDevFallback(request).orElseThrow(() -> new TenantNotResolvedException("Tenant could not be resolved"));
      final var httpSession = request.getSession(false);

      if (httpSession != null) {
        httpSession.removeAttribute(VernoConstants.ATTR_PUBLIC_NO_TENANT);
        httpSession.setAttribute(VernoConstants.ATTR_TENANT_ID, tenantId);
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

  @Nonnull
  private Optional<Long> resolveTenantIdWithDevFallback(@Nonnull HttpServletRequest request) {
    final var resolved = resolver.resolveTenantId(request);
    if (resolved.isPresent()) {
      return resolved;
    }

    if (isLocalhostRequest(request)) {
      final var devTenant = resolveDevTenantFromRunConfig()
              .flatMap(this::resolveTenantIdIfExists);

      if (devTenant.isPresent()) {
        return devTenant;
      }

      final var preferred = resolveTenantIdIfExists(7777L);
      if (preferred.isPresent()) {
        return preferred;
      }

      final var fallback = resolveFirstTenantIdFromDb();
      if (fallback.isPresent()) {
        return fallback;
      }
    }

    return Optional.empty();
  }

  @Nonnull
  private Optional<Long> resolveDevTenantFromRunConfig() {
    final var sysProp = System.getProperty("verno.dev.tenant-id");

    if (sysProp != null && !sysProp.isBlank()) {
      try {
        return Optional.of(Long.parseLong(sysProp));
      } catch (NumberFormatException ignored) {
      }
    }

    return Optional.empty();
  }

  private boolean isLocalhostRequest(@Nonnull final HttpServletRequest request) {
    final var host = request.getServerName();
    if (host == null) {
      return false;
    }

    return VernoConstants.LOCALHOST.equalsIgnoreCase(host)
            || VernoConstants.IP_172_0_0_1.equals(host)
            || VernoConstants.URL_DOUBLE_POINT_1.equals(host);
  }

  private Optional<Long> resolveFirstTenantIdFromDb() {
    try {
      final var query = entityManager.createNativeQuery("select id from mandants order by id asc limit 1");
      final var first = query.getSingleResult();
      if (first == null) {
        return Optional.empty();
      } else if (first instanceof Number n) {
        return Optional.of(n.longValue());
      }

      return Optional.of(Long.parseLong(first.toString()));
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }

  @Nonnull
  private Optional<Long> resolveTenantIdIfExists(@Nonnull final Long tenantId) {
    try {
      final var query = entityManager.createNativeQuery("select id from mandants where id = :id");
      query.setParameter(Publ.ID, tenantId);

      final var first = query.getSingleResult();
      if (first instanceof Number n) {
        return Optional.of(n.longValue());
      }

      return Optional.of(Long.parseLong(first.toString()));
    } catch (Exception ignored) {
      return Optional.empty();
    }
  }
}