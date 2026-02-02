package ch.verno.ui.tenant;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantResolutionFilter extends OncePerRequestFilter {

  @Nonnull
  private final SubdomainTenantResolver resolver;

  private final boolean strict;

  public TenantResolutionFilter(@Nonnull final SubdomainTenantResolver resolver,
                                final boolean strict) {
    this.resolver = resolver;
    this.strict = strict;
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain)
          throws ServletException, IOException {

    final var hostHeader = request.getHeader("Host");
    final var tenantKey = resolver.resolveTenantKey(hostHeader);

    if (strict && tenantKey.isBlank()) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    try {
      ch.verno.server.tenant.TenantContext.set(tenantKey);
      filterChain.doFilter(request, response);
    } finally {
      ch.verno.server.tenant.TenantContext.clear();
    }
  }
}