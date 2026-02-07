package ch.verno.common.gate;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class GlobalInterface {

  private static ApplicationContext context;

  @Autowired
  public GlobalInterface(@Nonnull final ApplicationContext applicationContext) {
    context = applicationContext;
  }

  @Nonnull
  public static GlobalInterface getInstance() {
    return context.getBean(GlobalInterface.class);
  }

  @Nonnull
  public <T> T getGate(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public <T> T getService(@Nonnull final Class<T> serviceClass) {
    return context.getBean(serviceClass);
  }

  @Nonnull
  public PasswordEncoder getPasswordEncoder() {
    return context.getBean(PasswordEncoder.class);
  }
}
