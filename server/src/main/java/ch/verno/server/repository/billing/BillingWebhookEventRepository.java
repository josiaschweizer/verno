package ch.verno.server.repository.billing;

import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.db.jpa.billing.SpringDataBillingWebhookEventJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BillingWebhookEventRepository {

  @Nonnull
  private final SpringDataBillingWebhookEventJpaRepository jpaRepository;

  public BillingWebhookEventRepository(@Nonnull final SpringDataBillingWebhookEventJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public BillingWebhookEventEntity save(@Nonnull final BillingWebhookEventEntity entity) {
    return jpaRepository.save(entity);
  }

  @Nonnull
  public Optional<BillingWebhookEventEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public Optional<BillingWebhookEventEntity> findByStripeEventId(@Nonnull final String stripeEventId) {
    return jpaRepository.findByStripeEventId(stripeEventId);
  }

  public boolean existsByStripeEventId(@Nonnull final String stripeEventId) {
    return jpaRepository.existsByStripeEventId(stripeEventId);
  }

  public boolean existsById(@Nonnull final Long id) {
    return jpaRepository.existsById(id);
  }

  @Nonnull
  public List<BillingWebhookEventEntity> findAll() {
    return jpaRepository.findAll();
  }

  public void deleteById(@Nonnull final Long id) {
    jpaRepository.deleteById(id);
  }
}