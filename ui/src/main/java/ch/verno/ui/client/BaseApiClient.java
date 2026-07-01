package ch.verno.ui.client;

import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.properties.tenant.TenantProperties;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public abstract class BaseApiClient {

  @Nonnull protected final RestClient restClient;
  @Nonnull private final Lazy<TenantProperties> tenantProperties;

  @Nullable private final BasicAuthRequest basicAuthRequest;

  protected BaseApiClient(@Nonnull final Injector injector,
                          @Nonnull final RestClient restClient,
                          @Nullable final BasicAuthRequest basicAuthRequest) {
    this.restClient = restClient;
    this.basicAuthRequest = basicAuthRequest;
    this.tenantProperties = Lazy.of(() -> injector.getInstance(TenantProperties.class));
  }

  @Nonnull
  protected static RestClient build(@Nonnull final String baseUrl) {
    return RestClient.builder()
            .baseUrl(baseUrl)
            .build();
  }

  @Nonnull
  protected static RestClient buildWithBasicAuth(@Nonnull final BasicAuthRequest request) {
    return RestClient.builder()
            .baseUrl(request.baseUrl())
            .defaultHeaders(headers -> headers.setBasicAuth(
                    request.username(),
                    request.password()
            ))
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
    applyToRequestHeader(rc);

    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  @Nonnull
  protected RestClient.RequestHeadersSpec<?> get(@Nonnull final String url,
                                                 @Nonnull final Map<String, String> headers) {
    RestClient.RequestHeadersSpec<?> rc = restClient.get().uri(url);
    applyToRequestHeader(rc);

    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  @Nonnull
  protected RestClient.RequestHeadersSpec<?> delete(@Nonnull final String url,
                                                    @Nonnull final Map<String, String> headers) {
    RestClient.RequestHeadersSpec<?> rc = restClient.delete().uri(url);
    applyToRequestHeader(rc);

    for (final var header : headers.entrySet()) {
      rc = rc.header(header.getKey(), header.getValue());
    }

    return rc;
  }

  private void applyToRequestHeader(@Nonnull final RestClient.RequestHeadersSpec<?> rc) {
    applyTenantHeader(rc);

    if (basicAuthRequest != null) {
      rc.headers(header -> header.setBasicAuth(basicAuthRequest.username(), basicAuthRequest.password()));
    }
  }

  private void applyTenantHeader(@Nonnull final RestClient.RequestHeadersSpec<?> spec) {
    final var tenant = tenantProperties.get().resolveCurrentTenant();
    tenant.ifPresent(tenantDto -> spec.headers(header -> {
      if (!header.containsHeader(VernoConstants.X_MANDANT)) {
        spec.header(VernoConstants.X_MANDANT, tenantDto.slug());
      }
    }));
  }

  public record BasicAuthRequest(@Nonnull String baseUrl,
                                 @Nonnull String username,
                                 @Nonnull String password) {

  }
}