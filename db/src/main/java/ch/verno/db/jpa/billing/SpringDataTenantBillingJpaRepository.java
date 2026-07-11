package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataTenantBillingJpaRepository extends
        AbstractEntityJpaRepository<TenantBillingEntity, Long> {

  @Nonnull
  Optional<TenantBillingEntity> findByTenant_Id(@Nonnull Long tenantId);

  @Nonnull
  Optional<TenantBillingEntity> findByStripeCustomerId(@Nonnull String stripeCustomerId);

  boolean existsByTenant_Id(@Nonnull Long tenantId);

}
