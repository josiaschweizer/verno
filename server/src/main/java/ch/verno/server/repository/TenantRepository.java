package ch.verno.server.repository;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.jpa.SpringDataTenantJpaRepository;
import ch.verno.server.tenant.UnscopedQuery;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@UnscopedQuery
public class TenantRepository {

  @Nonnull private final SpringDataTenantJpaRepository repository;

  public TenantRepository(@Nonnull final SpringDataTenantJpaRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  public List<TenantEntity> findAll() {
    return repository.findAll();
  }

  @Nonnull
  public Optional<TenantEntity> findById(@Nonnull final Long id) {
    return repository.findById(id);
  }

  @Nonnull
  public Optional<TenantEntity> findBySlug(@Nonnull final String slug) {
    return repository.findBySlug(slug);
  }

  @Nonnull
  public Optional<Long> findIdBySlug(@Nonnull final String slug) {
    return repository.findIdBySlug(slug);
  }

  public boolean existsBySlug(@Nonnull final String slug) {
    return repository.existsBySlug(slug);
  }

  @Nonnull
  public Long nextId() {
    return repository.nextId();
  }

  @Nonnull
  public TenantEntity save(@Nonnull final TenantEntity entity) {
    return repository.save(entity);
  }

  public long count() {
    return repository.count();
  }

}
