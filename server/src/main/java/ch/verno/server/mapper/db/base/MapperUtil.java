package ch.verno.server.mapper.db.base;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public final class MapperUtil {

  @Nonnull
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public static <E, D> Optional<D> mapOptional(@Nonnull final Optional<E> entity,
                                               @Nonnull final Function<E, D> mapper) {
    return entity.map(mapper);
  }

  @Nonnull
  public static <E, D> List<D> mapList(@Nonnull List<E> entities,
                                       @Nonnull Function<E, D> mapper) {
    return entities.stream()
            .map(mapper)
            .toList();
  }

}