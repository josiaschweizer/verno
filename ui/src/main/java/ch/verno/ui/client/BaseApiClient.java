package ch.verno.ui.client;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.VernoConstants;
import com.vaadin.flow.server.VaadinRequest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public abstract class BaseApiClient {

  @Nonnull private final GlobalInterface globalInterface;
  protected final RestClient restClient;

  protected BaseApiClient(@Nonnull final GlobalInterface globalInterface,
                          final RestClient restClient) {
    this.globalInterface = globalInterface;
    this.restClient = restClient;
  }

  protected static RestClient build(@Nonnull final String baseUrl) {
    return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
  }

  @Nonnull
  protected RestClient.RequestHeadersSpec<?> post(@Nonnull final String url,
                                                  @Nonnull final Map<String, String> headers,
                                                  @Nonnull final MediaType contentType,
                                                  @Nonnull final Object body) {

    RestClient.RequestHeadersSpec<?> rc = restClient.post()
            .uri(url)
            .contentType(contentType)
            .body(body);
    applyTenantHeader(rc);

    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  @Nonnull
  protected RestClient.RequestHeadersSpec<?> get(@Nonnull final String url,
                                                 @Nonnull final Map<String, String> headers) {
    RestClient.RequestHeadersSpec<?> rc = restClient.get()
            .uri(url);
    applyTenantHeader(rc);

    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  @Nonnull
  protected RestClient.RequestHeadersSpec<?> delete(@Nonnull final String url,
                                                    @Nonnull final Map<String, String> headers) {
    RestClient.RequestHeadersSpec<?> rc = restClient.delete()
            .uri(url);
    applyTenantHeader(rc);
    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  private void applyTenantHeader(@Nonnull final RestClient.RequestHeadersSpec<?> spec) {
    final var tenant = globalInterface.resolveTenant();

    if (tenant != null) {
      spec.header(VernoConstants.X_TENANT, tenant.getSlug());
    }
  }

  @Nullable
  protected String resolveSessionCookie() {
    final VaadinRequest request = VaadinRequest.getCurrent();
    if (request == null || request.getWrappedSession() == null) {
      return null;
    }
    return "JSESSIONID=" + request.getWrappedSession().getId();
  }
}