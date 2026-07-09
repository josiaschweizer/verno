package ch.verno.rpc.auth;

import ch.verno.common.rpc.auth.internal.InternalRpcTokenCodec;
import ch.verno.common.tenant.TenantContext;
import jakarta.annotation.Nonnull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.Duration;

public class InternalRpcAuthInterceptor implements ClientHttpRequestInterceptor {

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

    if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
      final var tenantId = TenantContext.get();
      final var token = tokenCodec.issue(authentication.getName(), tenantId, Duration.ofSeconds(30));
      request.getHeaders().setBearerAuth(token);
    }

    return execution.execute(request, body);
  }
}