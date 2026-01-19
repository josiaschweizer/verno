package ch.verno.common.gate;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class GlobalGate {

  private static ApplicationContext context;

  @Autowired
  public GlobalGate(@Nonnull final ApplicationContext applicationContext) {
    context = applicationContext;
  }

  @Nonnull
  public static GlobalGate getInstance() {
    return context.getBean(GlobalGate.class);
  }

  @Nonnull
  public <T> T getGate(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public <T> T getService(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }
}
