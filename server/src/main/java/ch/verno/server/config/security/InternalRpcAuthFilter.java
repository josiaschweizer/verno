package ch.verno.server.config.security;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.exception.rpc.auth.internal.InternalRpcTokenException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NonNls;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class InternalRpcAuthFilter extends OncePerRequestFilter {

  @NonNls public static final String RPC = Publ.SLASH + "rpc";
  @NonNls public static final String BEARER = "Bearer ";

  @Nonnull private final InternalRpcTokenCodec tokenCodec;

  public InternalRpcAuthFilter(@Nonnull final InternalRpcTokenCodec tokenCodec) {
    this.tokenCodec = tokenCodec;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull final HttpServletRequest request) {
    return !request.getRequestURI().startsWith(RPC);
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {
    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authHeader == null || !authHeader.startsWith(BEARER)) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing internal RPC token");
      return;
    }


    try {
      final var principal = tokenCodec.verify(authHeader.substring(BEARER.length()));
      final var authentication = UsernamePasswordAuthenticationToken.authenticated(
              principal.username(),
              null,
              New.list()
      );

      final var securityContext = SecurityContextHolder.createEmptyContext();
      securityContext.setAuthentication(authentication);
      SecurityContextHolder.setContext(securityContext);

      if (principal.tenantId() != null) {
        TenantContext.set(principal.tenantId());
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