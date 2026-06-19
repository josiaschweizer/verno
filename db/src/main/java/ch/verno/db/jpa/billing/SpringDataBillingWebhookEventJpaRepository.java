package ch.verno.db.jpa.billing;

import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataBillingWebhookEventJpaRepository extends
        AbstractEntityJpaRepository<BillingWebhookEventEntity, Long> {

  @Nonnull
  Optional<BillingWebhookEventEntity> findByStripeEventId(@Nonnull String stripeEventId);

  boolean existsByStripeEventId(@Nonnull String stripeEventId);

}
