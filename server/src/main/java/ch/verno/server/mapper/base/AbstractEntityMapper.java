package ch.verno.server.mapper.base;

import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractEntityMapper<E, D> {

  @Nonnull private MapperContext mapperContext;

  public AbstractEntityMapper() {
    this.mapperContext = MapperContext.empty();
  }

  public void setMapperContext(@Nonnull final MapperContext mapperContext) {
    this.mapperContext = mapperContext;
  }

  public void setContextMappers(@Nonnull final AbstractEntityMapper<?, ?>... mappers) {
    this.mapperContext = createContext(mappers);
  }

  @Nonnull
  protected MapperContext getMapperContext() {
    return mapperContext;
  }

  @Nonnull
  private static MapperContext createContext(@Nonnull final AbstractEntityMapper<?, ?>[] mappers) {
    return createContext(New.list(mappers));
  }

  @Nonnull
  private static MapperContext createContext(@Nonnull final List<AbstractEntityMapper<?, ?>> mappers) {
    final var context = MapperContext.empty();
    for (final var mapper : mappers) {
      putMapper(context, mapper);
    }

    return context;
  }

  protected static <M extends AbstractEntityMapper<?, ?>> void putMapper(@Nonnull final MapperContext context,
                                                                       @Nonnull final M mapper) {
    @SuppressWarnings("unchecked") final var type = (Class<M>) mapper.getClass();
    context.put(type, mapper);
  }

  @Nonnull
  protected <SE, SD, M extends AbstractEntityMapper<SE, SD>> SD mapReference(@Nullable final SE entity,
                                                                             @Nonnull final Class<M> mapperType,
                                                                             @Nonnull final Supplier<SD> emptySupplier,
                                                                             @Nonnull final Function<SE, SD> refSupplier) {
    if (entity == null) {
      return emptySupplier.get();
    }

    return getMapperContext().find(mapperType)
            .map(mapper -> mapper.toDto(entity))
            .orElseGet(() -> refSupplier.apply(entity));
  }

  @Nonnull
  protected <SE, SD, M extends AbstractEntityMapper<SE, SD>> List<SD> mapReferences(@Nullable final List<SE> entities,
                                                                                    @Nonnull final Class<M> mapperType,
                                                                                    @Nonnull final Supplier<SD> emptySupplier,
                                                                                    @Nonnull final Function<SE, SD> refSupplier) {
    if (entities == null || entities.isEmpty()) {
      return New.list();
    }

    return entities.stream().map(e -> mapReference(e, mapperType, emptySupplier, refSupplier)).toList();
  }

  @Nonnull
  public abstract D toDto(@Nonnull E entity);

  @Nonnull
  public abstract E toNewEntity(@Nonnull D dto);

  public abstract void updateEntity(@Nonnull E entity,
                                    @Nonnull D dto);
}