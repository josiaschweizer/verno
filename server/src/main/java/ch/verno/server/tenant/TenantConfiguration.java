package ch.verno.server.tenant;

import ch.verno.server.repository.TenantRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class TenantConfiguration {

  @Bean
  public TenantService tenantLookupService(@Nonnull final TenantRepository tenantRepository) {
    return new TenantService(tenantRepository);
  }

  @Bean
  public TenantResolver tenantResolver(@Nonnull final TenantProperties props,
                                       @Nonnull final TenantService lookupService) {
    return new TenantResolver(props, lookupService);
  }

  @Bean
  public FilterRegistrationBean<TenantFilter> tenantFilter(@Nonnull final TenantProperties props,
                                                           @Nonnull final TenantResolver resolver,
                                                           @Nonnull final EntityManager entityManager) {
    final var bean = new FilterRegistrationBean<TenantFilter>();
    bean.setFilter(new TenantFilter(props, resolver, entityManager));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
    return bean;
  }
}