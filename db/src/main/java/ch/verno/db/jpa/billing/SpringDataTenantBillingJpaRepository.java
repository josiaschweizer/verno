package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.TenantBillingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataTenantBillingJpaRepository extends JpaRepository<TenantBillingEntity, Long> {

  @Nonnull
  Optional<TenantBillingEntity> findByTenant_Id(@Nonnull Long tenantId);

  @Nonnull
  Optional<TenantBillingEntity> findByStripeCustomerId(@Nonnull String stripeCustomerId);

  boolean existsByTenant_Id(@Nonnull Long tenantId);

}
