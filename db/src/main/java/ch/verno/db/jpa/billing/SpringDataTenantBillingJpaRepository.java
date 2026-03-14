package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.TenantBillingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataTenantBillingJpaRepository extends JpaRepository<TenantBillingEntity, Long> {

  @Nonnull
  Optional<TenantBillingEntity> findByTenantId(@Nonnull Long tenantId);

  boolean existsByTenantId(@Nonnull Long tenantId);

}
