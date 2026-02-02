package ch.verno.ui.tenant;

import jakarta.annotation.Nonnull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Set;

@Configuration
public class TenantWebConfig {

  @Bean
  public SubdomainTenantResolver subdomainTenantResolver() {
    final var rootDomain = readEnvOrDefault("ROOT_DOMAIN", "verno.swiss");
    final var defaultTenant = readEnvOrDefault("DEFAULT_TENANT", "demo");

    final var devHosts = Set.of("localhost", "127.0.0.1", "::1");

    return new SubdomainTenantResolver(rootDomain, devHosts, defaultTenant);
  }

  @Bean
  public FilterRegistrationBean<TenantResolutionFilter> tenantResolutionFilter(@Nonnull final SubdomainTenantResolver resolver) {
    final var reg = new FilterRegistrationBean<>(new TenantResolutionFilter(resolver, true));
    reg.setOrder(Ordered.HIGHEST_PRECEDENCE);
    reg.addUrlPatterns("/*");
    return reg;
  }

  private static String readEnvOrDefault(@Nonnull final String key, @Nonnull final String defaultValue) {
    final var value = System.getenv(key);
    if (value == null) return defaultValue;
    final var trimmed = value.trim();
    return trimmed.isEmpty() ? defaultValue : trimmed;
  }
}