package ch.verno.server.service.extern.billing;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import ch.verno.common.db.service.extern.billing.IBillingWebhookEventService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.server.mapper.billing.BillingWebhookEventMapper;
import ch.verno.server.repository.billing.BillingWebhookEventRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillingWebhookEventService implements IBillingWebhookEventService {

  @Nonnull private final BillingWebhookEventRepository repository;

  public BillingWebhookEventService(@Nonnull final BillingWebhookEventRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  @Override
  @Transactional
  public BillingWebhookEventDto updateBillingWebhookEvent(@Nonnull final BillingWebhookEventDto dto) {
    final var id = dto.getId();
    if (id == null) {
      throw new IllegalStateException("id must not be null");
    }

    final var existingWebhookEvent = repository.findById(id)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_WEBHOOK_EVENT_BY_ID_NOT_FOUND));

    BillingWebhookEventMapper.updateEntity(existingWebhookEvent, dto);
    final var savedEntity = repository.save(existingWebhookEvent);
    return BillingWebhookEventMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public BillingWebhookEventDto createBillingWebhookEvent(@Nonnull final BillingWebhookEventDto dto) {
    if (dto.getStripeEventId().isBlank()) {
      throw new IllegalStateException("stripeEventId must not be blank");
    }
    if (dto.getEventType().isBlank()) {
      throw new IllegalStateException("eventType must not be blank");
    }
    if (dto.getStatus().isBlank()) {
      throw new IllegalStateException("status must not be blank");
    }

    try {
      final BillingWebhookEventEntity entity = BillingWebhookEventMapper.toEntity(dto);
      if (entity == null) {
        throw new IllegalStateException("BillingWebhookEventDto must not be null");
      }

      return BillingWebhookEventMapper.toDto(repository.save(entity));
    } catch (DataIntegrityViolationException exception) {
      final var existing = repository.findByStripeEventId(dto.getStripeEventId());

      if (existing.isPresent()) {
        return BillingWebhookEventMapper.toDto(existing.get());
      }

      throw exception;
    }
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingWebhookEventDto getBillingWebhookEventById(@Nonnull final Long id) {
    final var foundById = repository.findById(id);

    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.BILLING_WEBHOOK_EVENT_BY_ID_NOT_FOUND);
    }

    return BillingWebhookEventMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingWebhookEventDto getBillingWebhookEventByStripeEventId(@Nonnull final String stripeEventId) {
    final var foundByStripeEventId = repository.findByStripeEventId(stripeEventId);

    if (foundByStripeEventId.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.BILLING_WEBHOOK_EVENT_BY_STRIPE_EVENT_ID_NOT_FOUND);
    }

    return BillingWebhookEventMapper.toDto(foundByStripeEventId.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<BillingWebhookEventDto> getBillingWebhookEvents() {
    return repository.findAll().stream()
            .map(BillingWebhookEventMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional
  public BillingWebhookEventDto markBillingWebhookEventAsProcessed(@Nonnull final String stripeEventId) {
    final var webhookEvent = repository.findByStripeEventId(stripeEventId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_WEBHOOK_EVENT_BY_STRIPE_EVENT_ID_NOT_FOUND));

    webhookEvent.setProcessedAt(OffsetDateTime.now());

    final var savedEntity = repository.save(webhookEvent);
    return BillingWebhookEventMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public BillingWebhookEventDto updateBillingWebhookEventStatus(@Nonnull final String stripeEventId,
                                                                @Nonnull final String status) {
    final var webhookEvent = repository.findByStripeEventId(stripeEventId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_WEBHOOK_EVENT_BY_STRIPE_EVENT_ID_NOT_FOUND));

    webhookEvent.setStatus(status);

    final var savedEntity = repository.save(webhookEvent);
    return BillingWebhookEventMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public BillingWebhookEventDto saveBillingWebhookEvent(@Nonnull final BillingWebhookEventDto dto) {
    if (dto.getId() == null) {
      return createBillingWebhookEvent(dto);
    }
    return updateBillingWebhookEvent(dto);
  }
}