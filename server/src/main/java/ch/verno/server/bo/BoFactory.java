package ch.verno.server.bo;

import ch.verno.lib.New;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public final class BoFactory {

  @Nonnull private final ServerBean serverBean;
  @Nonnull private final Map<Class<?>, Object> cache;

  private BoFactory(@Nonnull final ServerBean serverBean) {
    this.serverBean = serverBean;
    this.cache = New.concurrentHashMap();
  }

  @Nonnull
  public static BoFactory getInstance(@Nonnull final ServerBean serverBean) {
    return new BoFactory(serverBean);
  }

  @Nonnull
  public <T> T get(@Nonnull final Class<T> type) {
    return type.cast(cache.computeIfAbsent(type, this::create));
  }

  @Nonnull
  private Object create(@Nonnull final Class<?> type) {
    try {
      return type
              .getDeclaredConstructor(ServerBean.class)
              .newInstance(serverBean);
    } catch (final NoSuchMethodException exception) {
      throw new IllegalStateException(
              "Business object requires a public constructor with ServerBean: "
                      + type.getName(),
              exception
      );
    } catch (final InstantiationException
                   | IllegalAccessException
                   | InvocationTargetException exception) {
      throw new IllegalStateException(
              "Could not create business object: " + type.getName(),
              exception
      );
    }
  }
}