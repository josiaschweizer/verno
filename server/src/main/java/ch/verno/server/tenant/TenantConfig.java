package ch.verno.server.config;

import ch.verno.server.tenant.TenantFilter;
import ch.verno.server.tenant.TenantResolver;
import jakarta.annotation.Nonnull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class TenantConfig {

  @Bean
  @Nonnull
  public TenantResolver tenantResolver() {
    return new TenantResolver();
  }

  @Bean
  @Nonnull
  public FilterRegistrationBean<TenantFilter> tenantFilter(
          @Nonnull final TenantResolver tenantResolver) {

    final var registration = new FilterRegistrationBean<TenantFilter>();

    registration.setFilter(new TenantFilter(tenantResolver));
    registration.addUrlPatterns("/rpc");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);

    return registration;
  }
}