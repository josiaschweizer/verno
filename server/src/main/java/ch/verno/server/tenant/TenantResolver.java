package ch.verno.server.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.common.properties.configprovider.VernoTenantConfigProvider;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.Optional;

public class TenantResolver {

  @Nonnull private final TenantService lookupService;
  @Nonnull private final VernoTenantConfigProvider tenantConfigProvider;

  public TenantResolver(@Nonnull final TenantService lookupService,
                        @Nonnull final VernoTenantConfigProvider tenantConfigProvider) {
    this.lookupService = lookupService;
    this.tenantConfigProvider = tenantConfigProvider;
  }

  @Nonnull
  public Optional<Long> resolveTenantId(@Nonnull final HttpServletRequest request) {
    final var host = safeLower(request.getServerName());
    final var slug = extractSubdomainSlug(host);

    if (slug != null && !slug.isBlank()) {
      final var id = lookupService.findTenantIdBySlug(slug);
      if (id.isPresent()) {
        return id;
      }
      throw new TenantNotResolvedException("Unknown tenant slug: " + slug + " (host=" + host + ")");
    }

    if (tenantConfigProvider.isAllowHeaderFallback()) {
      final var header = request.getHeader(tenantConfigProvider.getHeaderName());
      if (header != null && !header.isBlank()) {
        try {
          return Optional.of(Long.parseLong(header.trim()));
        } catch (final NumberFormatException e) {
          throw new TenantNotResolvedException("Invalid tenant header " + tenantConfigProvider.getHeaderName() + ": " + header, e);
        }
      }
    }

    return Optional.empty();
  }

  @Nullable
  private String extractSubdomainSlug(@Nonnull final String host) {
    if (host.isBlank()) {
      return null;
    }

    if (isIp(host) || host.equals(VernoConstants.LOCALHOST)) {
      return null;
    }

    if (host.endsWith(Publ.DOT + VernoConstants.LOCALHOST)) {
      final var parts = host.split("\\.");
      return parts.length >= 2 ? parts[0] : null;
    }

    for (final var base : tenantConfigProvider.getBaseDomains()) {
      final var baseLower = safeLower(base);
      if (baseLower.equals(VernoConstants.LOCALHOST)) {
        continue;
      }

      final var suffix = Publ.DOT + baseLower;
      if (host.endsWith(suffix)) {
        final var prefix = host.substring(0, host.length() - suffix.length());
        if (prefix.isBlank()) {
          return null;
        }

        final var labels = prefix.split("\\.");
        return labels.length >= 1 ? labels[0] : null;
      }
    }

    return null;
  }

  private boolean isIp(@Nonnull final String host) {
    return host.matches("^\\d{1,3}(\\.\\d{1,3}){3}$");
  }

  @Nonnull
  private String safeLower(@Nullable final String value) {
    return value == null ? Publ.EMPTY_STRING : value.trim().toLowerCase(Locale.ROOT);
  }
}