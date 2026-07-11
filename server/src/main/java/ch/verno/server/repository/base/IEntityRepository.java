package ch.verno.server.repository.base;

import ch.verno.server.config.tenant.UnscopedQuery;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public interface IEntityRepository<E, ID> {

  @Nonnull
  List<E> findAll();

  @Nonnull
  Page<E> findAll(@Nonnull Specification<E> specification,
                  @Nonnull Pageable pageable);

  @Nonnull
  Optional<E> findById(@Nonnull ID id);

  @Nonnull
  E save(@Nonnull E entity);

  @Nonnull
  E update(@Nonnull E entity);

  void delete(@Nonnull E entity);

  boolean deleteById(@Nonnull ID id);

  boolean existsById(@Nonnull ID id);

  long count();

  long countUnscoped();

  void flush();
}