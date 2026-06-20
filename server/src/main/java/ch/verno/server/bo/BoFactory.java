package ch.verno.server.bo;

import ch.verno.lib.New;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class BoFactory {

  @Nonnull private final ServerBean serverBean;
  @Nonnull private final Map<Class<?>, Object> cache;

  private BoFactory(@Nonnull final ServerBean serverBean) {
    this.cache = New.concurrentHashMap();
    this.serverBean = serverBean;
  }

  @Nonnull
  public static BoFactory getInstance(@Nonnull final ServerBean bean) {
    return new BoFactory(bean);
  }

  @Nonnull
  public <T> T get(@Nonnull final Class<T> type) {
    return type.cast(cache.computeIfAbsent(
            type,
            this::create
    ));
  }

  @Nonnull
  private Object create(@Nonnull final Class<?> type) {
    return serverBean.get(type);
  }
}