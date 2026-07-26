package ch.verno.server.mapper.base;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class MapperContext {

  @NonNls private static final String NO_VALUE_FOUND_FOR_TYPE = "No value found for type {}";

  private final Map<Class<?>, Object> values;

  public MapperContext() {
    values = new HashMap<>();
  }

  public MapperContext(@Nonnull final Map<Class<?>, Object> values) {
    this.values = values;
  }

  @Nonnull
  public static MapperContext empty() {
    return new MapperContext();
  }

  @Nonnull
  public <T> MapperContext put(@Nonnull final Class<T> type,
                               @Nonnull final T value) {
    values.put(type, value);
    return this;
  }

  @Nonnull
  public <T> Optional<T> find(@Nonnull final Class<T> type) {
    final var value = values.get(type);

    if (value == null) {
      LoggerFactory.getLogger(MapperContext.class).debug(NO_VALUE_FOUND_FOR_TYPE, type);
      return Optional.empty();
    }

    return Optional.of(type.cast(value));
  }

  @Nullable
  public <T> T getOrNull(@Nonnull final Class<T> type) {
    return find(type).orElse(null);
  }
}