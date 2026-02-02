package ch.verno.ui.tenant;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Set;

public class SubdomainTenantResolver {

  @Nonnull
  private final String rootDomain;

  @Nonnull
  private final Set<String> devHosts;

  @Nonnull
  private final String defaultTenant;

  public SubdomainTenantResolver(@Nonnull final String rootDomain,
                                 @Nonnull final Set<String> devHosts,
                                 @Nonnull final String defaultTenant) {
    this.rootDomain = normalizeHost(rootDomain);
    this.devHosts = devHosts;
    this.defaultTenant = defaultTenant;
  }

  @Nonnull
  public String resolveTenantKey(@Nullable final String hostHeader) {
    if (hostHeader == null || hostHeader.isBlank()) {
      return defaultTenant;
    }

    final var host = normalizeHost(stripPort(hostHeader));

    if (devHosts.contains(host)) {
      return defaultTenant;
    }

    if ("localhost".equals(host)) {
      return defaultTenant;
    }

    if (host.endsWith(".localhost")) {
      final var sub = host.substring(0, host.length() - ".localhost".length());
      return sanitizeTenantKeyOrDefault(sub);
    }

    if (host.equals(rootDomain)) {
      return defaultTenant;
    }

    if (host.endsWith("." + rootDomain)) {
      final var sub = host.substring(0, host.length() - ("." + rootDomain).length());
      return sanitizeTenantKeyOrDefault(sub);
    }

    return defaultTenant;
  }

  @Nonnull
  private String sanitizeTenantKeyOrDefault(@Nullable final String candidate) {
    if (candidate == null) return defaultTenant;
    final var c = candidate.trim().toLowerCase();
    if (c.isEmpty()) return defaultTenant;

    if (!c.matches("^[a-z0-9][a-z0-9-]{1,61}[a-z0-9]$")) {
      return defaultTenant;
    }
    return c;
  }

  @Nonnull
  private static String stripPort(@Nonnull final String host) {
    final var idx = host.indexOf(':');
    if (idx < 0) return host;
    return host.substring(0, idx);
  }

  @Nonnull
  private static String normalizeHost(@Nonnull final String host) {
    return host.trim().toLowerCase();
  }
}