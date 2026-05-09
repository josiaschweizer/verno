package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.lib.New;
import ch.verno.server.mapper.BaseMapperTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestTenantBillingMapper extends BaseMapperTest {

  @Test
  @DisplayName("toDto should return empty dto when entity is null")
  void toDto() {
    final var result = TenantBillingMapper.toDto(null);

    Assertions.assertNotNull(result);
    assertNullId(result.getId());
    Assertions.assertNull(result.getTenantId());
    assertEmptyString(result.getStripeCustomerId());
    assertEmptyString(result.getStripeSubscriptionId());
    assertEmptyString(result.getLastWebhookEventId());
  }

  @Test
  @DisplayName("toEntity should correctly map billing dto values to entity")
  void toEntity() {
    final var dto = new TenantBillingDto();
    dto.setId(11L);
    dto.setTenantId(22L);
    dto.setStripeCustomerId("cus_123");
    dto.setStripeSubscriptionId("sub_123");
    dto.setPlanKey(BillingPlanKey.PRO);
    dto.setSubscriptionStatus(BillingSubscriptionStatus.ACTIVE);
    dto.setPaymentStatus(BillingPaymentStatus.PAID);
    dto.setHasValidPaymentMethod(true);
    dto.setCurrentPeriodEnd(UPDATED_AT);
    dto.setGraceUntil(EXPIRES_AT);
    dto.setLastWebhookEventId("evt_123");

    final var entity = TenantBillingMapper.toEntity(dto, 22L);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals(11L, entity.getId());
    Assertions.assertEquals(22L, entity.getTenant().getId());
    Assertions.assertEquals("cus_123", entity.getStripeCustomerId());
    Assertions.assertEquals("sub_123", entity.getStripeSubscriptionId());
    Assertions.assertEquals(BillingPlanKey.PRO.name(), entity.getPlanKey());
    Assertions.assertEquals(BillingSubscriptionStatus.ACTIVE.name(), entity.getSubscriptionStatus());
    Assertions.assertEquals(BillingPaymentStatus.PAID.name(), entity.getPaymentStatus());
    Assertions.assertTrue(entity.isHasValidPaymentMethod());
    Assertions.assertEquals(UPDATED_AT, entity.getCurrentPeriodEnd());
    Assertions.assertEquals(EXPIRES_AT, entity.getGraceUntil());
    Assertions.assertEquals("evt_123", entity.getLastWebhookEventId());
  }

  @Test
  @DisplayName("updateEntity should overwrite mutable billing fields from dto")
  void updateEntity() {
    final var entity = new TenantBillingEntity(
            TenantEntity.ref(1L),
            BillingPlanKey.FREE.name(),
            BillingSubscriptionStatus.INACTIVE.name(),
            BillingPaymentStatus.UNPAID.name(),
            false,
            New.arrayList()
    );

    final var dto = new TenantBillingDto();
    dto.setStripeCustomerId("cus_new");
    dto.setStripeSubscriptionId("sub_new");
    dto.setPlanKey(BillingPlanKey.BASIC);
    dto.setSubscriptionStatus(BillingSubscriptionStatus.PAST_DUE);
    dto.setPaymentStatus(BillingPaymentStatus.FAILED);
    dto.setHasValidPaymentMethod(true);
    dto.setCurrentPeriodEnd(UPDATED_AT);
    dto.setGraceUntil(EXPIRES_AT);
    dto.setLastWebhookEventId("evt_new");

    TenantBillingMapper.updateEntity(entity, dto);

    Assertions.assertEquals("cus_new", entity.getStripeCustomerId());
    Assertions.assertEquals("sub_new", entity.getStripeSubscriptionId());
    Assertions.assertEquals(BillingPlanKey.BASIC.name(), entity.getPlanKey());
    Assertions.assertEquals(BillingSubscriptionStatus.PAST_DUE.name(), entity.getSubscriptionStatus());
    Assertions.assertEquals(BillingPaymentStatus.FAILED.name(), entity.getPaymentStatus());
    Assertions.assertTrue(entity.isHasValidPaymentMethod());
    Assertions.assertEquals(UPDATED_AT, entity.getCurrentPeriodEnd());
    Assertions.assertEquals(EXPIRES_AT, entity.getGraceUntil());
    Assertions.assertEquals("evt_new", entity.getLastWebhookEventId());
  }
}