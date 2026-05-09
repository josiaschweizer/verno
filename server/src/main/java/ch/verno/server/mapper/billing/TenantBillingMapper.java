package ch.verno.server.mapper.billing;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.type.billing.BillingLicenceOption;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class TenantBillingMapper {

  private TenantBillingMapper() {
  }

  @Nonnull
  public static TenantBillingDto toDto(@Nullable final TenantBillingEntity entity) {
    if (entity == null) {
      return new TenantBillingDto();
    }

    final var dto = new TenantBillingDto(
            entity.getId(),
            entity.getStripeCustomerId() == null ? Publ.EMPTY_STRING : entity.getStripeCustomerId(),
            entity.getStripeSubscriptionId() == null ? Publ.EMPTY_STRING : entity.getStripeSubscriptionId(),
            BillingPlanKey.valueOf(entity.getPlanKey()),
            BillingSubscriptionStatus.fromKey(entity.getSubscriptionStatus()),
            BillingPaymentStatus.fromKey(entity.getPaymentStatus()),
            entity.getCurrentPeriodEnd(),
            entity.getGraceUntil(),
            entity.isHasValidPaymentMethod(),
            entity.getLastWebhookEventId() == null ? Publ.EMPTY_STRING : entity.getLastWebhookEventId(),
            entity.getAdditionalLicenceOptions().stream()
                    .map(BillingLicenceOption::valueOf)
                    .toList()
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nullable
  public static TenantBillingEntity toEntity(@Nullable final TenantBillingDto dto,
                                             final long tenantId) {
    if (dto == null) {
      return null;
    }

    final var entity = new TenantBillingEntity(
            TenantEntity.ref(tenantId),
            dto.getPlanKey().name(),
            dto.getSubscriptionStatus().getKey(),
            dto.getPaymentStatus().getKey(),
            dto.isHasValidPaymentMethod(),
            dto.getAdditionalStringLicenceOptions()
    );

    if (dto.getId() != null && dto.getId() != 0) {
      entity.setId(dto.getId());
    }

    entity.setStripeCustomerId(dto.getStripeCustomerId().isBlank() ? null : dto.getStripeCustomerId());
    entity.setStripeSubscriptionId(dto.getStripeSubscriptionId().isBlank() ? null : dto.getStripeSubscriptionId());
    entity.setCurrentPeriodEnd(dto.getCurrentPeriodEnd());
    entity.setGraceUntil(dto.getGraceUntil());
    entity.setLastWebhookEventId(dto.getLastWebhookEventId().isBlank() ? null : dto.getLastWebhookEventId());

    return entity;
  }

  public static void updateEntity(@Nonnull final TenantBillingEntity entity,
                                  @Nonnull final TenantBillingDto dto) {
    entity.setStripeCustomerId(dto.getStripeCustomerId().isBlank() ? null : dto.getStripeCustomerId());
    entity.setStripeSubscriptionId(dto.getStripeSubscriptionId().isBlank() ? null : dto.getStripeSubscriptionId());
    entity.setPlanKey(dto.getPlanKey().name());
    entity.setSubscriptionStatus(dto.getSubscriptionStatus().getKey());
    entity.setPaymentStatus(dto.getPaymentStatus().getKey());
    entity.setCurrentPeriodEnd(dto.getCurrentPeriodEnd());
    entity.setGraceUntil(dto.getGraceUntil());
    entity.setHasValidPaymentMethod(dto.isHasValidPaymentMethod());
    entity.setLastWebhookEventId(dto.getLastWebhookEventId().isBlank() ? null : dto.getLastWebhookEventId());
  }
}