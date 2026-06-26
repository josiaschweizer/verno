package ch.verno.ui.client;

import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.properties.tenant.TenantProperties;
import com.google.inject.Injector;
import com.vaadin.flow.server.VaadinRequest;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public abstract class BaseApiClient {

  @Nonnull private final Injector injector;
  @Nonnull protected final RestClient restClient;
  @Nonnull private final Lazy<TenantProperties> tenantProperties;

  protected BaseApiClient(@Nonnull final Injector injector,
                          @Nonnull final RestClient restClient) {
    this.injector = injector;
    this.restClient = restClient;
    this.tenantProperties = Lazy.of(() -> injector.getInstance(TenantProperties.class));
  }

  @Nonnull
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
    RestClient.RequestHeadersSpec<?> rc = restClient.get().uri(url);
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
    final var tenant = tenantProperties.get().resolveCurrentTenant();
    tenant.ifPresent(tenantDto -> spec.header(VernoConstants.X_MANDANT, tenantDto.slug()));
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