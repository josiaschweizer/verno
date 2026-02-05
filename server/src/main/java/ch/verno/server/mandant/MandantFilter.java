package ch.verno.server.mandant;

import ch.verno.common.exceptions.server.mandant.MandantNotResolvedException;
import ch.verno.common.lib.Routes;
import ch.verno.common.mandant.MandantContext;
import ch.verno.db.entity.mandant.MandantFilters;
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

public class MandantFilter extends OncePerRequestFilter {

  private static final String ATTR_PUBLIC_NO_TENANT = "PUBLIC_NO_TENANT";

  @Nonnull private final MandantProperties props;
  @Nonnull private final MandantResolver resolver;
  @Nonnull private final EntityManager entityManager;

  public MandantFilter(@Nonnull final MandantProperties props,
                       @Nonnull final MandantResolver resolver,
                       @Nonnull final EntityManager entityManager) {
    this.props = props;
    this.resolver = resolver;
    this.entityManager = entityManager;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull HttpServletRequest request) {
    if (!props.isEnabled()) return true;

    String path = request.getRequestURI();
    String ctx = request.getContextPath();
    if (ctx != null && !ctx.isEmpty() && path.startsWith(ctx)) {
      path = path.substring(ctx.length());
    }

    final var session = request.getSession(false);
    if (session != null && Boolean.TRUE.equals(session.getAttribute(ATTR_PUBLIC_NO_TENANT))) {
      return true;
    }

    if (path.startsWith(ApiUrl.BASE_API)) {
      return true;
    }

    switch (path) {
      case Publ.SLASH + Routes.MANDANT_NOT_FOUND, Publ.SLASH + Routes.MANDANT_NOT_FOUND + Publ.SLASH -> {
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
      final Long mandantId = resolver.resolveMandantId(request).orElseThrow(() -> new MandantNotResolvedException("Mandant could not be resolved"));

      final var httpSession = request.getSession(false);
      if (httpSession != null) {
        httpSession.removeAttribute(ATTR_PUBLIC_NO_TENANT);
      }

      MandantContext.set(mandantId);

      final var session = entityManager.unwrap(Session.class);
      session.enableFilter(MandantFilters.MANDANT_FILTER).setParameter(MandantFilters.PARAM_MANDANT_ID, mandantId);

      filterChain.doFilter(request, response);
    } catch (MandantNotResolvedException e) {
      response.sendRedirect(request.getContextPath() + Routes.MANDANT_NOT_FOUND);
    } finally {
      MandantContext.clear();
    }
  }
}