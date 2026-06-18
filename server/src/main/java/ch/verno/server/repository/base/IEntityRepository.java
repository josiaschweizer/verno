package ch.verno.server.repository.base;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IEntityRepository<E,ID> {

  @Nonnull
  List<E> findAll();

  @Nonnull
  Optional<E> findById(@Nonnull ID id);

  @Nonnull
  E save(@Nonnull E entity);

  @Nonnull
  E update(@Nonnull E entity);

  void delete(@Nonnull E entity);

  void deleteById(@Nonnull ID id);

  boolean existsById(@Nonnull ID id);

  long count();

  void flush();
}