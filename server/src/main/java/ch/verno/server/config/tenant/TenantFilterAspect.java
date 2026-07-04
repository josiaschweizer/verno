package ch.verno.server.config.tenant;

import jakarta.annotation.Nonnull;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

  @Nonnull private final TenantFilterEnabler enabler;

  public TenantFilterAspect(@Nonnull final TenantFilterEnabler enabler) {
    this.enabler = enabler;
  }

  @Before(
          "within(ch.verno.server.repository..*) && " +
                  "!@within(ch.verno.server.config.tenant.UnscopedQuery) && " +
                  "!@annotation(ch.verno.server.config.tenant.UnscopedQuery)"
  )
  public void enableTenantFilter() {
    enabler.enable();
  }
}