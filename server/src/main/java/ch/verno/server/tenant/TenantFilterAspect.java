package ch.verno.server.tenant;

import jakarta.annotation.Nonnull;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

  private final TenantFilterEnabler enabler;

  public TenantFilterAspect(@Nonnull final TenantFilterEnabler enabler) {
    this.enabler = enabler;
  }

  @Before(
          "within(@org.springframework.stereotype.Repository *) && " +
                  "!within(@ch.verno.server.tenant.UnscopedQuery *) && " +
                  "!@annotation(ch.verno.server.tenant.UnscopedQuery)"
  )
  public void enableTenantFilter() {
    enabler.enable();
  }
}