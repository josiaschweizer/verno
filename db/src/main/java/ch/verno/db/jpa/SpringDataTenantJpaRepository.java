package ch.verno.db.jpa;

import ch.verno.db.entity.tenant.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataTenantJpaRepository extends JpaRepository<TenantEntity, Long> {

  Optional<TenantEntity> findBySlug(String slug);

  @Query("select m.id from TenantEntity m where m.slug = :slug")
  Optional<Long> findIdBySlug(String slug);

  boolean existsBySlug(String slug);

  @Query(value = "select nextval('mandants_id_seq')", nativeQuery = true)
  Long nextId();
}
