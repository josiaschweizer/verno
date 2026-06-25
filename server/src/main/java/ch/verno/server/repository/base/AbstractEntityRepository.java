package ch.verno.server.repository.base;

import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityRepository<ENTITY, ID, R extends AbstractEntityJpaRepository<ENTITY, ID>> implements IEntityRepository<ENTITY, ID> {

  @Nonnull private final R repository;

  protected AbstractEntityRepository(@Nonnull final R repository) {
    this.repository = repository;
  }

  @Nonnull
  protected R getRepository() {
    return repository;
  }

  @Nonnull
  @Override
  public List<ENTITY> findAll() {
    return repository.findAll();
  }

  @Nonnull
  @Override
  public Page<ENTITY> findAll(@Nonnull final Specification<ENTITY> specification,
                              @Nonnull final Pageable pageable) {
    return repository.findAll(specification, pageable);
  }

  @Nonnull
  @Override
  public Optional<ENTITY> findById(@Nonnull final ID id) {
    return repository.findById(id);
  }

  @Nonnull
  @Override
  public ENTITY save(@Nonnull final ENTITY entity) {
    return repository.save(entity);
  }

  @Nonnull
  @Override
  public ENTITY update(@Nonnull final ENTITY entity) {
    return repository.save(entity);
  }

  @Override
  public void delete(@Nonnull final ENTITY entity) {
    repository.delete(entity);
  }

  @Override
  public boolean deleteById(@Nonnull final ID id) {
    if (existsById(id)) {
      repository.deleteById(id);
      return true;
    }

    return false;
  }

  @Override
  public boolean existsById(@Nonnull final ID id) {
    return repository.existsById(id);
  }

  @Override
  public long count() {
    return repository.count();
  }

  @Override
  public void flush() {
    repository.flush();
  }

}