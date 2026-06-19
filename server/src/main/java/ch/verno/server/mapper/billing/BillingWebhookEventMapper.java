package ch.verno.server.mapper.billing;

import ch.verno.common.type.billing.BillingWebhookEventStatus;
import ch.verno.contract.dto.table.billing.BillingWebhookEventDto;
import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class BillingWebhookEventMapper implements IEntityMapper<BillingWebhookEventEntity, BillingWebhookEventDto> {

  @Nonnull
  @Override
  public BillingWebhookEventDto toSimpleDto(@Nonnull final BillingWebhookEventEntity entity) {

    final var dto = BillingWebhookEventDto.empty();
    dto.setId(entity.getId());

    dto.setStripeEventId(entity.getStripeEventId());
    dto.setEventType(entity.getEventType());
    dto.setStatus(BillingWebhookEventStatus.fromKey(entity.getStatus()));

    dto.setProcessedAt(entity.getProcessedAt());
    dto.setCreatedAt(entity.getCreatedAt());

    dto.setPayloadJson(entity.getPayloadJson());

    return dto;
  }

  @Nonnull
  @Override
  public BillingWebhookEventEntity toNewEntity(@Nonnull final BillingWebhookEventDto dto) {

    final var entity = new BillingWebhookEventEntity(
            dto.getStripeEventId(),
            dto.getEventType(),
            dto.getStatus().getKey(),
            dto.getPayloadJson()
    );

    updateEntity(entity, dto);

    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final BillingWebhookEventEntity entity,
                           @Nonnull final BillingWebhookEventDto dto) {

    entity.setStripeEventId(dto.getStripeEventId());
    entity.setEventType(dto.getEventType());
    entity.setStatus(dto.getStatus().getKey());

    entity.setPayloadJson(dto.getPayloadJson());
    entity.setProcessedAt(dto.getProcessedAt());
    if (dto.getCreatedAt() != null) {
      entity.setCreatedAt(dto.getCreatedAt());
    }
  }
}