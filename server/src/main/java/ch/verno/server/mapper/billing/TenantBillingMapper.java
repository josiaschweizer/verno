package ch.verno.server.mapper.billing;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.common.type.billing.BillingPaymentStatus;
import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.lib.Publ;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantBillingMapper implements IEntityMapper<TenantBillingEntity, TenantBillingDto> {

  @Nonnull
  @Override
  public TenantBillingDto toSimpleDto(@Nonnull final TenantBillingEntity entity) {
    final var dto = TenantBillingDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);

    dto.setStripeCustomerId(nullToEmpty(entity.getStripeCustomerId()));
    dto.setStripeSubscriptionId(nullToEmpty(entity.getStripeSubscriptionId()));
    dto.setPlanKey(BillingPlanKey.valueOf(entity.getPlanKey()));
    dto.setSubscriptionStatus(BillingSubscriptionStatus.fromKey(entity.getSubscriptionStatus()));
    dto.setPaymentStatus(BillingPaymentStatus.fromKey(entity.getPaymentStatus()));
    dto.setCurrentPeriodEnd(entity.getCurrentPeriodEnd());
    dto.setGraceUntil(entity.getGraceUntil());
    dto.setHasValidPaymentMethod(entity.isHasValidPaymentMethod());
    dto.setLastWebhookEventId(nullToEmpty(entity.getLastWebhookEventId()));
    dto.setAdditionalLicenceOptions(toLicenceOptions(entity.getAdditionalLicenceOptions()));

    return dto;
  }

  @Nonnull
  @Override
  public TenantBillingEntity toNewEntity(@Nonnull final TenantBillingDto dto) {
    final var entity = TenantBillingEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final TenantBillingEntity entity,
                           @Nonnull final TenantBillingDto dto) {
    entity.setStripeCustomerId(nullToEmpty(dto.getStripeCustomerId()));
    entity.setStripeSubscriptionId(nullToEmpty(dto.getStripeSubscriptionId()));
    entity.setPlanKey(dto.getPlanKey().name());
    entity.setSubscriptionStatus(dto.getSubscriptionStatus().name());
    entity.setPaymentStatus(dto.getPaymentStatus().name());
    entity.setCurrentPeriodEnd(dto.getCurrentPeriodEnd());
    entity.setGraceUntil(dto.getGraceUntil());
    entity.setHasValidPaymentMethod(dto.isHasValidPaymentMethod());
    entity.setLastWebhookEventId(nullToEmpty(dto.getLastWebhookEventId()));
    entity.setAdditionalLicenceOptions(dto.getAdditionalStringLicenceOptions());
  }

  @Nonnull
  private List<BillingLicenceOption> toLicenceOptions(@Nonnull final List<String> values) {
    return values.stream()
            .map(BillingLicenceOption::valueOf)
            .toList();
  }

  @Nonnull
  private String nullToEmpty(final String value) {
    return value == null ? Publ.EMPTY_STRING : value;
  }
}