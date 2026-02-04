package ch.verno.server.mandant;

import jakarta.annotation.Nonnull;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MandantFilterAspect {

  private final MandantFilterEnabler enabler;

  public MandantFilterAspect(@Nonnull final MandantFilterEnabler enabler) {
    this.enabler = enabler;
  }

  @Before(
          "within(@org.springframework.stereotype.Repository *) && " +
                  "!within(@ch.verno.server.mandant.UnscopedQuery *) && " +
                  "!@annotation(ch.verno.server.mandant.UnscopedQuery)"
  )
  public void enableMandantFilter() {
    enabler.enable();
  }
}