package ch.verno.server.repository.base;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T> {

  @Nonnull
  Optional<T> findById(@Nonnull final Long id);

  @Nonnull
  List<T> findAll();

  void save(@Nonnull final T entity);
}
