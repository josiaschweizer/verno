package ch.verno.ui.tenant;

import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class UiTenantConfig {

  @Bean
  @Nonnull
  public UiTenantResolver uiTenantResolver(@Nonnull final Injector injector) {
    return new UiTenantResolver(injector);
  }

  @Bean
  @Nonnull
  public FilterRegistrationBean<UiTenantFilter> uiTenantFilter(@Nonnull final UiTenantResolver tenantResolver) {
    final var registration = new FilterRegistrationBean<UiTenantFilter>();
    registration.setFilter(new UiTenantFilter(tenantResolver));
    registration.addUrlPatterns("/*");
    registration.setOrder(Ordered.HIGHEST_PRECEDENCE);

    return registration;
  }
}