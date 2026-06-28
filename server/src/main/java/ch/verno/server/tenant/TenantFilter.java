package ch.verno.server.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

  @Nonnull private final TenantResolver tenantResolver;

  public TenantFilter(@Nonnull final TenantResolver tenantResolver) {
    this.tenantResolver = tenantResolver;
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {

    try {
      final var tenantIdOptional = tenantResolver.resolveTenantId(request);
      tenantIdOptional.ifPresent(TenantContext::set);

      filterChain.doFilter(request, response);
    } catch (final TenantNotResolvedException exception) {
      response.sendError(
              HttpServletResponse.SC_BAD_REQUEST,
              exception.getMessage()
      );
    } finally {
      TenantContext.clear();
    }
  }
}