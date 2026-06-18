package ch.verno.server.repository.tenant;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.jpa.tenant.SpringDataTenantJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import ch.verno.server.tenant.UnscopedQuery;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@UnscopedQuery
public class TenantRepository extends AbstractEntityRepository<TenantEntity, Long, SpringDataTenantJpaRepository> {

  public TenantRepository(@Nonnull final SpringDataTenantJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<TenantEntity> findBySlug(@Nonnull final String slug) {
    return getRepository().findBySlug(slug);
  }

  @Nonnull
  public Optional<Long> findIdBySlug(@Nonnull final String slug) {
    return getRepository().findIdBySlug(slug);
  }

  @Nonnull
  public Optional<Long> findIdByName(@Nonnull final String name) {
    return getRepository().findIdByName(name);
  }

  public boolean existsBySlug(@Nonnull final String slug) {
    return getRepository().existsBySlug(slug);
  }

  @Nonnull
  public Long nextId() {
    return getRepository().nextId();
  }
}