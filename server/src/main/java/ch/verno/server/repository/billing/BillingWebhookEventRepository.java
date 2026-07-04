package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.db.jpa.billing.SpringDataBillingWebhookEventJpaRepository;
import ch.verno.server.config.tenant.UnscopedQuery;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@UnscopedQuery
public class BillingWebhookEventRepository extends AbstractEntityRepository<
        BillingWebhookEventEntity,
        Long,
        SpringDataBillingWebhookEventJpaRepository> {

  public BillingWebhookEventRepository(@Nonnull final SpringDataBillingWebhookEventJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<BillingWebhookEventEntity> findByStripeEventId(@Nonnull final String stripeEventId) {
    return getRepository().findByStripeEventId(stripeEventId);
  }

  public boolean existsByStripeEventId(@Nonnull final String stripeEventId) {
    return getRepository().existsByStripeEventId(stripeEventId);
  }

}
