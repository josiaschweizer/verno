package ch.verno.db.jpa.tenant;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import ch.verno.db.jpa.gender.SpringDataGenderJpaRepository;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataTenantJpaRepository extends AbstractEntityJpaRepository<TenantEntity, Long> {

  @NonNls String SELECT_TENANT_ID_FROM_NAME_SQL = "select m.id from TenantEntity m where m.name = :name";
  @NonNls String SELECT_TENANT_ID_BY_SLUG_SQL = "select m.id from TenantEntity m where m.slug = :slug";
  @NonNls String SELECT_NEXT_TENANT_SQL = "select nextval('mandants_id_seq')";

  @Nonnull
  Optional<TenantEntity> findBySlug(@Nonnull String slug);

  @Nonnull
  @Query(SELECT_TENANT_ID_BY_SLUG_SQL)
  Optional<Long> findIdBySlug(@Nonnull String slug);

  @Nonnull
  @Query(SELECT_TENANT_ID_FROM_NAME_SQL)
  Optional<Long> findIdByName(@Nonnull String name);

  boolean existsBySlug(@Nonnull String slug);

  @Query(value = SELECT_NEXT_TENANT_SQL, nativeQuery = true)
  Long nextId();
}