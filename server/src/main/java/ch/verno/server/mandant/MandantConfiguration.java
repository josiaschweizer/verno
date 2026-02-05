package ch.verno.server.mandant;

import ch.verno.server.repository.MandantRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Configuration
public class MandantConfiguration {

  @Bean
  public MandantLookupService mandantLookupService(@Nonnull final MandantRepository mandantRepository) {
    return new MandantLookupService(mandantRepository);
  }

  @Bean
  public MandantResolver mandantResolver(@Nonnull final MandantProperties props,
                                         @Nonnull final MandantLookupService lookupService) {
    return new MandantResolver(props, lookupService);
  }

  @Bean
  public FilterRegistrationBean<MandantFilter> mandantFilter(@Nonnull final MandantProperties props,
                                                             @Nonnull final MandantResolver resolver,
                                                             @Nonnull final EntityManager entityManager) {
    final var bean = new FilterRegistrationBean<MandantFilter>();
    bean.setFilter(new MandantFilter(props, resolver, entityManager));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
    return bean;
  }
}