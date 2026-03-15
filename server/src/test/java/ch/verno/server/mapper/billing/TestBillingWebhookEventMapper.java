package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import ch.verno.common.db.type.billing.BillingWebhookEventStatus;
import ch.verno.db.entity.billing.BillingWebhookEventEntity;
import ch.verno.server.mapper.BaseMapperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestBillingWebhookEventMapper extends BaseMapperTest {

  @Test
  @DisplayName("toDto should return empty dto when entity is null")
  void toDto() {
    final var result = BillingWebhookEventMapper.toDto(null);

    Assertions.assertNotNull(result);
    assertNullId(result.getId());
    assertEmptyString(result.getStripeEventId());
    assertEmptyString(result.getEventType());
    assertEmptyString(result.getStatus().getKey());
    assertEmptyString(result.getPayloadJson());
  }

  @Test
  @DisplayName("toEntity should map webhook dto values to entity")
  void toEntity() {
    final var dto = new BillingWebhookEventDto();
    dto.setId(5L);
    dto.setStripeEventId("evt_123");
    dto.setEventType("invoice.paid");
    dto.setStatus(BillingWebhookEventStatus.RECEIVED);
    dto.setPayloadJson("{\"id\":\"evt_123\"}");
    dto.setProcessedAt(UPDATED_AT);
    dto.setCreatedAt(CREATED_AT);

    final var entity = BillingWebhookEventMapper.toEntity(dto);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals(5L, entity.getId());
    Assertions.assertEquals("evt_123", entity.getStripeEventId());
    Assertions.assertEquals("invoice.paid", entity.getEventType());
    Assertions.assertEquals(BillingWebhookEventStatus.RECEIVED.name(), entity.getStatus());
    Assertions.assertEquals(UPDATED_AT, entity.getProcessedAt());
    Assertions.assertEquals(CREATED_AT, entity.getCreatedAt());
    Assertions.assertEquals("{\"id\":\"evt_123\"}", entity.getPayloadJson());
  }

  @Test
  @DisplayName("updateEntity should overwrite webhook event fields from dto")
  void updateEntity() {
    final var entity = new BillingWebhookEventEntity(
            "evt_old",
            "invoice.created",
            BillingWebhookEventStatus.RECEIVED.name(),
            "{}"
    );

    final var dto = new BillingWebhookEventDto();
    dto.setStripeEventId("evt_new");
    dto.setEventType("invoice.payment_failed");
    dto.setStatus(BillingWebhookEventStatus.FAILED);
    dto.setProcessedAt(UPDATED_AT);
    dto.setPayloadJson("{\"failed\":true}");

    BillingWebhookEventMapper.updateEntity(entity, dto);

    Assertions.assertEquals("evt_new", entity.getStripeEventId());
    Assertions.assertEquals("invoice.payment_failed", entity.getEventType());
    Assertions.assertEquals(BillingWebhookEventStatus.FAILED.name(), entity.getStatus());
    Assertions.assertEquals(UPDATED_AT, entity.getProcessedAt());
    Assertions.assertEquals("{\"failed\":true}", entity.getPayloadJson());
  }
}