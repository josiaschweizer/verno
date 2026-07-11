package ch.verno.ui.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.properties.tenant.TenantProperties;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Locale;
import java.util.Optional;

public class UiTenantResolver {

  @Nonnull private final Lazy<TenantProperties> tenantClient;

  public UiTenantResolver(@Nonnull final Injector injector) {
    this.tenantClient = Lazy.of(() -> injector.getInstance(TenantProperties.class));
  }

  @Nonnull
  public Optional<Long> resolveTenantId(@Nullable final String host) {
    final var normalizedHost = safeLower(host);
    final var slug = extractSubdomainSlug(normalizedHost);

    if (slug == null || slug.isBlank()) {
      return resolveDevelopmentTenantId();
    }

    final var tenantId = tenantClient.get().getTenantIdBySlug(slug);
    if (tenantId.isPresent()) {
      return tenantId;
    }

    throw new TenantNotResolvedException("Unknown tenant slug: " + slug + " (host=" + normalizedHost + ")");
  }

  @Nullable
  private String extractSubdomainSlug(@Nonnull final String host) {
    if (host.isBlank()
            || isIpAddress(host)
            || host.equals(VernoConstants.LOCALHOST)) {
      return null;
    }

    if (host.endsWith(Publ.DOT + VernoConstants.LOCALHOST)) {
      final var parts = host.split("\\.");
      return parts.length >= 2 ? parts[0] : null;
    }

    final var productionDomain = "verno-app.ch"; //TODO PROPERTY!!!
    final var productionSuffix = Publ.DOT + productionDomain;

    if (!host.endsWith(productionSuffix)) {
      return null;
    }

    final var prefix = host.substring(
            0,
            host.length() - productionSuffix.length()
    );

    if (prefix.isBlank()) {
      return null;
    }

    return prefix.split("\\.")[0];
  }

  @Nonnull
  private Optional<Long> resolveDevelopmentTenantId() {
    final var value = System.getProperty("verno.dev.tenant-id");

    if (value == null || value.isBlank()) {
      return Optional.empty();
    }

    try {
      return Optional.of(Long.parseLong(value.trim()));
    } catch (final NumberFormatException exception) {
      throw new TenantNotResolvedException("Invalid development tenant id: " + value, exception);
    }
  }

  private boolean isIpAddress(@Nonnull final String host) {
    return host.matches("^\\d{1,3}(\\.\\d{1,3}){3}$");
  }

  @Nonnull
  private String safeLower(@Nullable final String value) {
    return value == null
            ? Publ.EMPTY_STRING
            : value.trim().toLowerCase(Locale.ROOT);
  }
}