package ch.verno.gateway.config.security;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.common.rpc.auth.pub.ResourceAccessTokenCodec;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Publ;
import ch.verno.lib.exception.rpc.auth.ResourceAccessTokenException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NonNls;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Authorizes requests under {@code ApiUrl.PUBLIC_AUTH_BASE_API} using a
 * short-lived, resource-scoped access token supplied as the
 * {@code access_token} query parameter, instead of session or Basic
 * authentication.
 */
@Component
public class ResourceAccessFilter extends OncePerRequestFilter {

  @NonNls public static final String ERROR_MISSING_ACCESS_TOKEN = "Missing access token";
  @NonNls public static final String ERROR_INVALID_OR_EXPIRED_ACCESS_TOKEN = "Invalid or expired access token";

  @Nonnull private final ResourceAccessTokenCodec tokenCodec;

  public ResourceAccessFilter(@Nonnull final ResourceAccessTokenCodec tokenCodec) {
    this.tokenCodec = tokenCodec;
  }

  @Override
  protected boolean shouldNotFilter(@Nonnull final HttpServletRequest request) {
    return !request.getRequestURI().startsWith(ApiUrl.PUBLIC_AUTH_BASE_API);
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {
    final var token = request.getParameter(ApiQueryParam.ACCESS_TOKEN);
    final var resourceId = extractResourceId(request.getRequestURI());
    if (token == null || resourceId == null) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_MISSING_ACCESS_TOKEN);
      return;
    }

    try {
      final var principal = tokenCodec.verify(token, resourceId);
      TenantContext.set(principal.tenantId());
      filterChain.doFilter(request, response);
    } catch (final ResourceAccessTokenException exception) {
      response.sendError(HttpServletResponse.SC_FORBIDDEN, ERROR_INVALID_OR_EXPIRED_ACCESS_TOKEN);
    } finally {
      TenantContext.clear();
    }
  }

  /**
   * The last path segment is treated as the resource identifier (e.g. the temp-file token).
   */
  @Nullable
  private String extractResourceId(@Nonnull final String requestUri) {
    final var withoutTrailingSlash = requestUri.endsWith(Publ.SLASH)
            ? requestUri.substring(0, requestUri.length() - 1)
            : requestUri;
    final var lastSlash = withoutTrailingSlash.lastIndexOf(Publ.Char.SLASH);
    return lastSlash >= 0 && lastSlash < withoutTrailingSlash.length() - 1
            ? withoutTrailingSlash.substring(lastSlash + 1)
            : null;
  }
}