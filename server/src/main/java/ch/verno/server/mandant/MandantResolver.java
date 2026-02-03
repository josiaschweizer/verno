package ch.verno.server.mandant;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.Optional;

public class MandantResolver {

  @Nonnull
  private final MandantProperties props;

  @Nonnull
  private final MandantLookupService lookupService;

  public MandantResolver(@Nonnull final MandantProperties props,
                         @Nonnull final MandantLookupService lookupService) {
    this.props = props;
    this.lookupService = lookupService;
  }

  @Nonnull
  public Optional<Long> resolveMandantId(@Nonnull final HttpServletRequest request) {
    // 1) Subdomain (empfohlen)
    final var host = safeLower(request.getServerName());
    final var slug = extractSubdomainSlug(host);

    if (slug != null && !slug.isBlank()) {
      final var id = lookupService.findMandantIdBySlug(slug);
      if (id.isPresent()) {
        return id;
      }
      throw new MandantNotResolvedException("Unknown mandant slug: " + slug + " (host=" + host + ")");
    }

    // 2) Header fallback (nur falls du es willst)
    if (props.isAllowHeaderFallback()) {
      final var header = request.getHeader(props.getHeaderName());
      if (header != null && !header.isBlank()) {
        try {
          return Optional.of(Long.parseLong(header.trim()));
        } catch (final NumberFormatException e) {
          throw new MandantNotResolvedException("Invalid mandant header " + props.getHeaderName() + ": " + header, e);
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

    // IPs oder "localhost" ohne Subdomain
    if (isIp(host) || host.equals("localhost")) {
      return null;
    }

    // Für dev: demo.localhost -> slug = demo
    if (host.endsWith(".localhost")) {
      final var parts = host.split("\\.");
      return parts.length >= 2 ? parts[0] : null;
    }

    // Prod: fcsg.verno-app.ch -> slug = fcsg (base domain = verno-app.ch)
    for (final var base : props.getBaseDomains()) {
      final var baseLower = safeLower(base);
      if (baseLower.equals("localhost")) {
        continue;
      }

      final var suffix = "." + baseLower;
      if (host.endsWith(suffix)) {
        final var prefix = host.substring(0, host.length() - suffix.length()); // z.B. "fcsg" oder "a.b"
        if (prefix.isBlank()) {
          return null;
        }
        // nur erste Label als slug (fcsg bei fcsg.verno-app.ch)
        final var labels = prefix.split("\\.");
        return labels.length >= 1 ? labels[0] : null;
      }
    }

    return null;
  }

  private boolean isIp(@Nonnull final String host) {
    // sehr einfache Heuristik (IPv4)
    return host.matches("^\\d{1,3}(\\.\\d{1,3}){3}$");
  }

  @Nonnull
  private String safeLower(@Nullable final String value) {
    return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
  }
}