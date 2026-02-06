package ch.verno.server.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.Optional;

public class TenantResolver {

  @Nonnull
  private final TenantProperties props;

  @Nonnull
  private final TenantLookupService lookupService;

  public TenantResolver(@Nonnull final TenantProperties props,
                        @Nonnull final TenantLookupService lookupService) {
    this.props = props;
    this.lookupService = lookupService;
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

    if (props.isAllowHeaderFallback()) {
      final var header = request.getHeader(props.getHeaderName());
      if (header != null && !header.isBlank()) {
        try {
          return Optional.of(Long.parseLong(header.trim()));
        } catch (final NumberFormatException e) {
          throw new TenantNotResolvedException("Invalid tenant header " + props.getHeaderName() + ": " + header, e);
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

    if (isIp(host) || host.equals(Publ.LOCALHOST)) {
      return null;
    }

    if (host.endsWith(Publ.DOT + Publ.LOCALHOST)) {
      final var parts = host.split("\\.");
      return parts.length >= 2 ? parts[0] : null;
    }

    for (final var base : props.getBaseDomains()) {
      final var baseLower = safeLower(base);
      if (baseLower.equals(Publ.LOCALHOST)) {
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