package ch.verno.contract.dto.table.billing;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.common.type.billing.BillingPaymentStatus;
import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TenantBillingDto extends BaseDto<Long> {

  @Nonnull private String stripeCustomerId;
  @Nonnull private String stripeSubscriptionId;
  @Nonnull private BillingPlanKey planKey;
  @Nonnull private BillingSubscriptionStatus subscriptionStatus;
  @Nonnull private BillingPaymentStatus paymentStatus;
  @Nullable private OffsetDateTime currentPeriodEnd;
  @Nullable private OffsetDateTime graceUntil;
  private boolean hasValidPaymentMethod;
  @Nonnull private String lastWebhookEventId;
  @Nonnull private List<BillingLicenceOption> additionalLicenceOptions;

  private TenantBillingDto() {
    setId(null);
    this.stripeCustomerId = Publ.EMPTY_STRING;
    this.stripeSubscriptionId = Publ.EMPTY_STRING;
    this.planKey = BillingPlanKey.FREE;
    this.subscriptionStatus = BillingSubscriptionStatus.INACTIVE;
    this.paymentStatus = BillingPaymentStatus.UNPAID;
    this.currentPeriodEnd = null;
    this.graceUntil = null;
    this.hasValidPaymentMethod = false;
    this.lastWebhookEventId = Publ.EMPTY_STRING;
    this.additionalLicenceOptions = New.list();
  }

  public TenantBillingDto(@Nonnull final String stripeCustomerId,
                          @Nonnull final String stripeSubscriptionId,
                          @Nonnull final BillingPlanKey planKey,
                          @Nonnull final BillingSubscriptionStatus subscriptionStatus,
                          @Nonnull final BillingPaymentStatus paymentStatus,
                          @Nullable final OffsetDateTime currentPeriodEnd,
                          @Nullable final OffsetDateTime graceUntil,
                          final boolean hasValidPaymentMethod,
                          @Nonnull final String lastWebhookEventId,
                          @Nonnull final List<BillingLicenceOption> additionalLicenceOptions) {
    this(null, stripeCustomerId, stripeSubscriptionId, planKey, subscriptionStatus, paymentStatus, currentPeriodEnd, graceUntil, hasValidPaymentMethod, lastWebhookEventId, additionalLicenceOptions);
  }

  public TenantBillingDto(@Nullable final Long id,
                          @Nonnull final String stripeCustomerId,
                          @Nonnull final String stripeSubscriptionId,
                          @Nonnull final BillingPlanKey planKey,
                          @Nonnull final BillingSubscriptionStatus subscriptionStatus,
                          @Nonnull final BillingPaymentStatus paymentStatus,
                          @Nullable final OffsetDateTime currentPeriodEnd,
                          @Nullable final OffsetDateTime graceUntil,
                          final boolean hasValidPaymentMethod,
                          @Nonnull final String lastWebhookEventId,
                          @Nonnull final List<BillingLicenceOption> additionalLicenceOptions) {
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
    this.additionalLicenceOptions = additionalLicenceOptions;
  }

  @Nonnull
  public static TenantBillingDto empty() {
    return new TenantBillingDto();
  }

  @Nonnull
  public static TenantBillingDto createDefaultDevDto(@Nonnull final Long tenantId) {
    final var defaultDevDto = new TenantBillingDto(
            null,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            BillingPlanKey.BASIC,
            BillingSubscriptionStatus.ACTIVE,
            BillingPaymentStatus.PAID,
            OffsetDateTime.now().plusYears(5),
            null,
            true,
            Publ.EMPTY_STRING,
            Arrays.stream(BillingLicenceOption.values()).toList()
    );

    defaultDevDto.setTenantId(tenantId); // tenant id has for saving the dto
    return defaultDevDto;
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
  public BillingPlanKey getPlanKey() {
    return planKey;
  }

  public void setPlanKey(@Nonnull final BillingPlanKey planKey) {
    this.planKey = planKey;
  }

  @Nonnull
  public BillingSubscriptionStatus getSubscriptionStatus() {
    return subscriptionStatus;
  }

  public void setSubscriptionStatus(@Nonnull final BillingSubscriptionStatus subscriptionStatus) {
    this.subscriptionStatus = subscriptionStatus;
  }

  @Nonnull
  public BillingPaymentStatus getPaymentStatus() {
    return paymentStatus;
  }

  public void setPaymentStatus(@Nonnull final BillingPaymentStatus paymentStatus) {
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
  public List<BillingLicenceOption> getAdditionalLicenceOptions() {
    return additionalLicenceOptions;
  }

  @Nonnull
  public List<String> getAdditionalStringLicenceOptions() {
    return New.list(additionalLicenceOptions.stream().map(BillingLicenceOption::name).toList());
  }

  public void setAdditionalLicenceOptions(@Nonnull final List<BillingLicenceOption> additionalLicenceOptions) {
    this.additionalLicenceOptions.clear();
    this.additionalLicenceOptions.addAll(additionalLicenceOptions);
  }

  @Nonnull
  public String getLastWebhookEventId() {
    return lastWebhookEventId;
  }

  public void setLastWebhookEventId(@Nonnull final String lastWebhookEventId) {
    this.lastWebhookEventId = lastWebhookEventId;
  }

  public boolean isActiveBilling() {
    return BillingSubscriptionStatus.ACTIVE.equals(subscriptionStatus) ||
            BillingSubscriptionStatus.TRIAL.equals(subscriptionStatus);
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