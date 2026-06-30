package ch.verno.server.service.entity.billing;

import ch.verno.common.type.billing.BillingWebhookEventStatus;
import ch.verno.contract.dto.table.billing.BillingWebhookEventDto;
import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.billing.BillingWebhookEventMapper;
import ch.verno.server.repository.billing.BillingWebhookEventRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
@Transactional
public class BillingWebhookEventService extends AbstractEntityServiceLongId<
        BillingWebhookEventEntity,
        BillingWebhookEventDto,
        BillingWebhookEventRepository,
        BillingWebhookEventMapper
        > {

  public BillingWebhookEventService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(BillingWebhookEventRepository.class), serverBean.get(BillingWebhookEventMapper.class));
  }

  @Nonnull
  protected BillingWebhookEventDto createWithFallback(@Nonnull final BillingWebhookEventDto dto) {
    try {
      return create(dto);
    } catch (DataIntegrityViolationException exception) {
      return getRepository().findByStripeEventId(dto.getStripeEventId())
              .map(getMapper()::toSimpleDto)
              .orElseThrow(() -> exception);
    }
  }

  @Transactional(readOnly = true)
  public boolean isAlreadyProcessed(@Nonnull final String stripeEventId) {
    return getRepository().findByStripeEventId(stripeEventId)
            .map(e -> e.getStatus().equals(BillingWebhookEventStatus.PROCESSED.name()))
            .orElse(false);
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<BillingWebhookEventDto> findByStripeEventId(@Nonnull final String stripeEventId) {
    return getRepository().findByStripeEventId(stripeEventId)
            .map(getMapper()::toSimpleDto);
  }

  public void markProcessed(@Nonnull final String stripeEventId) {
    final var dto = findByStripeEventId(stripeEventId)
            .orElseThrow(() -> new IllegalStateException("BillingWebhookEvent not found for stripeEventId: " + stripeEventId));
    dto.setStatus(BillingWebhookEventStatus.PROCESSED);
    dto.setProcessedAt(OffsetDateTime.now());
    save(dto);
  }

  public void markFailed(@Nonnull final String stripeEventId, @Nonnull final String message) {
    final var dto = findByStripeEventId(stripeEventId)
            .orElseThrow(() -> new IllegalStateException("BillingWebhookEvent not found for stripeEventId: " + stripeEventId));
    dto.setStatus(BillingWebhookEventStatus.FAILED);
    dto.setProcessedAt(OffsetDateTime.now());
    dto.setPayloadJson(message);
    save(dto);
  }

  public void resetToReceived(@Nonnull final String stripeEventId) {
    findByStripeEventId(stripeEventId).ifPresent(existing -> {
      existing.setStatus(BillingWebhookEventStatus.RECEIVED);
      save(existing);
    });
  }
}