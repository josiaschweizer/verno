package ch.verno.gateway.config.tenant;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.endpoint.properties.tenant.TenantResource;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayTenantFilter extends OncePerRequestFilter {

  @Nonnull private final Lazy<TenantResource> tenantResource;

  public GatewayTenantFilter(@Nonnull final RpcFactory rpcFactory) {
    this.tenantResource = Lazy.of(() -> rpcFactory.create(TenantResource.class));
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {
    final var tenantSlug = request.getHeader(VernoConstants.X_MANDANT);

    try {
      if (tenantSlug != null && !tenantSlug.isBlank()) {
        final var resolvedTenantId = tenantResource.get().getTenantIdBySlug(tenantSlug);
        resolvedTenantId.ifPresent(TenantContext::set);
      }
      filterChain.doFilter(request, response);
    } finally {
      TenantContext.clear();
    }
  }
}