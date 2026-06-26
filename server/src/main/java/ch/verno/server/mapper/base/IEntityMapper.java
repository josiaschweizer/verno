package ch.verno.server.mapper.base;

import jakarta.annotation.Nonnull;

public interface IEntityMapper<E, D> {

  @Nonnull
  D toSimpleDto(@Nonnull E entity);

  @Nonnull
  default D toDto(@Nonnull final E entity,
                  @Nonnull final MapperContext context) {
    return toSimpleDto(entity);
  }

  @Nonnull
  E toNewEntity(@Nonnull D dto);

  void updateEntity(@Nonnull E entity,
                    @Nonnull D dto);
}