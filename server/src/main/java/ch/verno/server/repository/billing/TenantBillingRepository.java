package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.db.jpa.billing.SpringDataTenantBillingJpaRepository;
import ch.verno.server.config.tenant.UnscopedQuery;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TenantBillingRepository extends AbstractEntityRepository<
        TenantBillingEntity,
        Long,
        SpringDataTenantBillingJpaRepository> {

  public TenantBillingRepository(@Nonnull final SpringDataTenantBillingJpaRepository repository) {
    super(repository);
  }

  public Optional<TenantBillingEntity> findByTenantId(@Nonnull final Long tenantId) {
    return getRepository().findByTenant_Id(tenantId);
  }

  @Nonnull
  @UnscopedQuery
  public Optional<TenantBillingEntity> findByStripeCustomerId(@Nonnull String stripeCustomerId) {
    return getRepository().findByStripeCustomerId(stripeCustomerId);
  }

  public boolean existsByTenantId(@Nonnull final Long tenantId) {
    return getRepository().existsByTenant_Id(tenantId);
  }


}
