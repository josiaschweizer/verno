package ch.verno.ui.verno.security;

import ch.verno.common.db.service.IAppUserService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private static final String BEARER_PREFIX = "Bearer ";

  private final JwtUtil jwtUtil;
  private final IAppUserService appUserService;

  public JwtAuthenticationFilter(
          @Nonnull final JwtUtil jwtUtil,
          @Nonnull final IAppUserService appUserService) {
    this.jwtUtil = jwtUtil;
    this.appUserService = appUserService;
  }

  @Override
  protected void doFilterInternal(
          @Nonnull final HttpServletRequest request,
          @Nonnull final HttpServletResponse response,
          @Nonnull final FilterChain filterChain) throws ServletException, IOException {
    final var requestURI = request.getRequestURI();
    if (!requestURI.startsWith("/api/") ||
            requestURI.equals("/login") ||
            requestURI.startsWith("/VAADIN/")) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      final var token = extractToken(request);

      if (token != null && jwtUtil.validateToken(token)) {
        authenticateUser(token);
        log.debug("JWT authentication successful for request: {}", request.getRequestURI());
      }
    } catch (Exception e) {
      log.debug("JWT authentication failed for {}: {}", request.getRequestURI(), e.getMessage());
      SecurityContextHolder.clearContext();
    }

    filterChain.doFilter(request, response);
  }

  private String extractToken(@Nonnull final HttpServletRequest request) {
    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
      return authHeader.substring(BEARER_PREFIX.length());
    }

    return null;
  }

  private void authenticateUser(@Nonnull final String token) {
    final var email = jwtUtil.extractEmail(token);
    final var tenantId = jwtUtil.extractTenantId(token);

    final var user = appUserService.findByUserNameMandantId(email, tenantId)
            .orElseThrow(() -> new UsernameNotFoundException(
                    "User '" + email + "' with tenant " + tenantId + " not found"));

    final var authToken = new CustomUserPasswordAuthenticationToken(user, user.getPasswordHash());
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
}