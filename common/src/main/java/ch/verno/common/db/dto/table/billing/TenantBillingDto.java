package ch.verno.common.db.dto.table.billing;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.Objects;

public class TenantBillingDto extends BaseDto {

  @Nonnull private String stripeCustomerId;
  @Nonnull private String stripeSubscriptionId;
  @Nonnull private String planKey;
  @Nonnull private String subscriptionStatus;
  @Nonnull private String paymentStatus;
  @Nullable private OffsetDateTime currentPeriodEnd;
  @Nullable private OffsetDateTime graceUntil;
  private boolean hasValidPaymentMethod;
  @Nonnull private String lastWebhookEventId;

  public TenantBillingDto() {
    setId(null);
    this.stripeCustomerId = Publ.EMPTY_STRING;
    this.stripeSubscriptionId = Publ.EMPTY_STRING;
    this.planKey = BillingPlanKey.FREE.name();
    this.subscriptionStatus = BillingSubscriptionStatus.INACTIVE.name();
    this.paymentStatus = BillingPaymentStatus.UNPAID.name();
    this.currentPeriodEnd = null;
    this.graceUntil = null;
    this.hasValidPaymentMethod = false;
    this.lastWebhookEventId = Publ.EMPTY_STRING;
  }

  public TenantBillingDto(@Nullable final Long id,
                          @Nonnull final String stripeCustomerId,
                          @Nonnull final String stripeSubscriptionId,
                          @Nonnull final String planKey,
                          @Nonnull final String subscriptionStatus,
                          @Nonnull final String paymentStatus,
                          @Nullable final OffsetDateTime currentPeriodEnd,
                          @Nullable final OffsetDateTime graceUntil,
                          final boolean hasValidPaymentMethod,
                          @Nonnull final String lastWebhookEventId) {
    setId(id);
    this.stripeCustomerId = stripeCustomerId;
    this.stripeSubscriptionId = stripeSubscriptionId;
    this.planKey = planKey;
    this.subscriptionStatus = subscriptionStatus;
    this.paymentStatus = paymentStatus;
    this.currentPeriodEnd = currentPeriodEnd;
    this.graceUntil = graceUntil;
    this.hasValidPaymentMethod = hasValidPaymentMethod;
    this.lastWebhookEventId = lastWebhookEventId;
  }

  @Nonnull
  public String getStripeCustomerId() {
    return stripeCustomerId;
  }

  public void setStripeCustomerId(@Nonnull final String stripeCustomerId) {
    this.stripeCustomerId = stripeCustomerId;
  }

  @Nonnull
  public String getStripeSubscriptionId() {
    return stripeSubscriptionId;
  }

  public void setStripeSubscriptionId(@Nonnull final String stripeSubscriptionId) {
    this.stripeSubscriptionId = stripeSubscriptionId;
  }

  @Nonnull
  public String getPlanKey() {
    return planKey;
  }

  public void setPlanKey(@Nonnull final String planKey) {
    this.planKey = planKey;
  }

  @Nonnull
  public String getSubscriptionStatus() {
    return subscriptionStatus;
  }

  public void setSubscriptionStatus(@Nonnull final String subscriptionStatus) {
    this.subscriptionStatus = subscriptionStatus;
  }

  @Nonnull
  public String getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(@Nonnull final String paymentStatus) {
    this.paymentStatus = paymentStatus;
  }

  @Nullable
  public OffsetDateTime getCurrentPeriodEnd() {
    return currentPeriodEnd;
  }

  public void setCurrentPeriodEnd(@Nullable final OffsetDateTime currentPeriodEnd) {
    this.currentPeriodEnd = currentPeriodEnd;
  }

  @Nullable
  public OffsetDateTime getGraceUntil() {
    return graceUntil;
  }

  public void setGraceUntil(@Nullable final OffsetDateTime graceUntil) {
    this.graceUntil = graceUntil;
  }

  public boolean isHasValidPaymentMethod() {
    return hasValidPaymentMethod;
  }

  public void setHasValidPaymentMethod(final boolean hasValidPaymentMethod) {
    this.hasValidPaymentMethod = hasValidPaymentMethod;
  }

  @Nonnull
  public String getLastWebhookEventId() {
    return lastWebhookEventId;
  }

  public void setLastWebhookEventId(@Nonnull final String lastWebhookEventId) {
    this.lastWebhookEventId = lastWebhookEventId;
  }

  public boolean isActiveBilling() {
    return BillingSubscriptionStatus.ACTIVE.name().equals(subscriptionStatus)
            || BillingSubscriptionStatus.TRIAL.name().equals(subscriptionStatus);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof TenantBillingDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}