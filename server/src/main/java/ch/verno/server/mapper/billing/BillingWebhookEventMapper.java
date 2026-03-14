package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class BillingWebhookEventMapper {

  private BillingWebhookEventMapper() {
  }

  @Nonnull
  public static BillingWebhookEventDto toDto(@Nullable final BillingWebhookEventEntity entity) {
    if (entity == null) {
      return new BillingWebhookEventDto();
    }

    return new BillingWebhookEventDto(
            entity.getId(),
            entity.getStripeEventId(),
            entity.getEventType(),
            entity.getStatus(),
            entity.getProcessedAt(),
            entity.getCreatedAt(),
            entity.getPayloadJson() == null ? Publ.EMPTY_STRING : entity.getPayloadJson()
    );
  }

  @Nullable
  public static BillingWebhookEventEntity toEntity(@Nullable final BillingWebhookEventDto dto) {
    if (dto == null) {
      return null;
    }

    final var entity = new BillingWebhookEventEntity(
            dto.getStripeEventId(),
            dto.getEventType(),
            dto.getStatus(),
            dto.getPayloadJson().isBlank() ? null : dto.getPayloadJson()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    }

    entity.setProcessedAt(dto.getProcessedAt());
    entity.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : java.time.OffsetDateTime.now());

    return entity;
  }

  public static void updateEntity(@Nonnull final BillingWebhookEventEntity entity,
                                  @Nonnull final BillingWebhookEventDto dto) {
    entity.setStripeEventId(dto.getStripeEventId());
    entity.setEventType(dto.getEventType());
    entity.setStatus(dto.getStatus());
    entity.setProcessedAt(dto.getProcessedAt());
    entity.setPayloadJson(dto.getPayloadJson().isBlank() ? null : dto.getPayloadJson());
  }
}