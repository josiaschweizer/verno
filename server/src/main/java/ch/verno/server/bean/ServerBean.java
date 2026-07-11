package ch.verno.server.bean;

import jakarta.annotation.Nonnull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ServerBean {

  @Nonnull private final ApplicationContext applicationContext;

  public ServerBean(@Nonnull final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Nonnull
  public <T> T get(@Nonnull final Class<T> type) {
    return applicationContext.getBean(type);
  }
}