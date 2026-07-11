package ch.verno.rpc.auth;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.Duration;

public class InternalRpcAuthInterceptor implements ClientHttpRequestInterceptor {

  @NonNls public static final String SYSTEM = "system";

  @Nonnull private final InternalRpcTokenCodec tokenCodec;

  public InternalRpcAuthInterceptor(@Nonnull final InternalRpcTokenCodec tokenCodec) {
    this.tokenCodec = tokenCodec;
  }

  @Nonnull
  @Override
  public ClientHttpResponse intercept(@Nonnull final HttpRequest request,
                                      @Nonnull final byte[] body,
                                      @Nonnull final ClientHttpRequestExecution execution) throws IOException {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();

    final var username = isValidAuthentication(authentication) && !(authentication instanceof AnonymousAuthenticationToken) ?
            authentication.getName() :
            SYSTEM;

    final var token = tokenCodec.issue(username, TenantContext.get(), Duration.ofSeconds(30));
    request.getHeaders().setBearerAuth(token);

    return execution.execute(request, body);
  }

  private boolean isValidAuthentication(@Nullable final Authentication authentication) {
    return authentication != null && authentication.isAuthenticated();
  }
}