package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.InternalRpcTokenCodec;
import ch.verno.common.rpc.auth.InternalRpcTokenException;
import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class InternalRpcAuthFilter extends OncePerRequestFilter {

  @Nonnull private final InternalRpcTokenCodec tokenCodec;

  public InternalRpcAuthFilter(@Nonnull final InternalRpcTokenCodec tokenCodec) {
    this.tokenCodec = tokenCodec;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull final HttpServletRequest request) {
    return !request.getRequestURI().startsWith("/rpc");
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain)
          throws ServletException, IOException {

    final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    try {
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        final var principal = tokenCodec.verify(authorizationHeader.substring("Bearer ".length()));

        final var authentication = UsernamePasswordAuthenticationToken.authenticated(
                principal.username(), null, List.of());
        final var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        if (principal.tenantId() != null) {
          TenantContext.set(principal.tenantId());
        }
      }

      filterChain.doFilter(request, response);

    } catch (final InternalRpcTokenException exception) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid internal RPC token");
    } finally {
      SecurityContextHolder.clearContext();
      TenantContext.clear();
    }
  }
}