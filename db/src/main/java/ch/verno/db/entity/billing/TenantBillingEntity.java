package ch.verno.db.entity.billing;

import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "tenant_billing", schema = "public")
@EntityListeners(TenantEntityListener.class)
public class TenantBillingEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Nonnull
  @Column(name = "updated_at", nullable = false)
  private OffsetDateTime updatedAt = OffsetDateTime.now();

  @Column(name = "stripe_customer_id")
  private String stripeCustomerId;

  @Column(name = "stripe_subscription_id")
  private String stripeSubscriptionId;

  @Nonnull
  @Column(name = "plan_key", nullable = false)
  private String planKey;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
          name = "tenant_billing_licence_option",
          schema = "public",
          joinColumns = @JoinColumn(name = "tenant_billing_id")
  )
  @Column(name = "licence_option", nullable = false)
  private List<String> additionalLicenceOptions;

  @Nonnull
  @Column(name = "subscription_status", nullable = false)
  private String subscriptionStatus;

  @Nonnull
  @Column(name = "payment_status", nullable = false)
  private String paymentStatus;

  @Column(name = "current_period_end")
  private OffsetDateTime currentPeriodEnd;

  @Column(name = "grace_until")
  private OffsetDateTime graceUntil;

  @Column(name = "has_valid_payment_method", nullable = false)
  private boolean hasValidPaymentMethod = false;

  @Column(name = "last_webhook_event_id")
  private String lastWebhookEventId;

  protected TenantBillingEntity() {
    // JPA
  }

  public TenantBillingEntity(@Nonnull final TenantEntity tenant,
                             @Nonnull final String planKey,
                             @Nonnull final String subscriptionStatus,
                             @Nonnull final String paymentStatus,
                             final boolean hasValidPaymentMethod,
                             @Nonnull final List<String> additionalLicenceOptions) {
    setTenant(tenant);
    this.planKey = planKey;
    this.subscriptionStatus = subscriptionStatus;
    this.paymentStatus = paymentStatus;
    this.hasValidPaymentMethod = hasValidPaymentMethod;
    this.additionalLicenceOptions = additionalLicenceOptions;
  }

  @Nonnull
  public static TenantBillingEntity createDefault(@Nonnull final TenantEntity tenant) {
    return new TenantBillingEntity(
            tenant,
            BillingPlanKey.FREE.name(),
            BillingSubscriptionStatus.INACTIVE.name(),
            BillingPaymentStatus.UNPAID.name(),
            false,
            New.arrayList()
    );
  }

  @PreUpdate
  public void preUpdate() {
    updatedAt = OffsetDateTime.now();
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Nonnull
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nonnull final OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getStripeCustomerId() {
    return stripeCustomerId;
  }

  public void setStripeCustomerId(final String stripeCustomerId) {
    this.stripeCustomerId = stripeCustomerId;
  }

  public String getStripeSubscriptionId() {
    return stripeSubscriptionId;
  }

  public void setStripeSubscriptionId(final String stripeSubscriptionId) {
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
  public List<String> getAdditionalLicenceOptions() {
    return additionalLicenceOptions;
  }

  public void setAdditionalLicenceOptions(@Nonnull final List<String> additionalLicenceOptions) {
    this.additionalLicenceOptions = additionalLicenceOptions;
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

  public OffsetDateTime getCurrentPeriodEnd() {
    return currentPeriodEnd;
  }

  public void setCurrentPeriodEnd(final OffsetDateTime currentPeriodEnd) {
    this.currentPeriodEnd = currentPeriodEnd;
  }

  public OffsetDateTime getGraceUntil() {
    return graceUntil;
  }

  public void setGraceUntil(final OffsetDateTime graceUntil) {
    this.graceUntil = graceUntil;
  }

  public boolean isHasValidPaymentMethod() {
    return hasValidPaymentMethod;
  }

  public void setHasValidPaymentMethod(final boolean hasValidPaymentMethod) {
    this.hasValidPaymentMethod = hasValidPaymentMethod;
  }

  public String getLastWebhookEventId() {
    return lastWebhookEventId;
  }

  public void setLastWebhookEventId(final String lastWebhookEventId) {
    this.lastWebhookEventId = lastWebhookEventId;
  }
}