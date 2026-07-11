package ch.verno.server.mapper.base;

import jakarta.annotation.Nonnull;

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
    final var context = MapperContext.empty();

    for (final var mapper : mappers) {
      putMapper(context, mapper);
    }

    return context;
  }

  private static <M extends AbstractEntityMapper<?, ?>> void putMapper(@Nonnull final MapperContext context,
                                                                       @Nonnull final M mapper) {
    @SuppressWarnings("unchecked") final var type = (Class<M>) mapper.getClass();
    context.put(type, mapper);
  }

  @Nonnull
  public abstract D toDto(@Nonnull E entity);

  @Nonnull
  public abstract E toNewEntity(@Nonnull D dto);

  public abstract void updateEntity(@Nonnull E entity,
                                    @Nonnull D dto);
}