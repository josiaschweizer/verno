package ch.verno.common.db.dto.table.billing;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.type.billing.BillingWebhookEventStatus;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.Objects;

public class BillingWebhookEventDto extends BaseDto {

  @Nonnull
  private String stripeEventId;

  @Nonnull
  private String eventType;

  @Nonnull
  private BillingWebhookEventStatus status;

  @Nullable
  private OffsetDateTime processedAt;

  @Nullable
  private OffsetDateTime createdAt;

  @Nonnull
  private String payloadJson;

  public BillingWebhookEventDto() {
    setId(null);
    this.stripeEventId = Publ.EMPTY_STRING;
    this.eventType = Publ.EMPTY_STRING;
    this.status = BillingWebhookEventStatus.NONE;
    this.processedAt = null;
    this.createdAt = null;
    this.payloadJson = Publ.EMPTY_STRING;
  }

  public BillingWebhookEventDto(@Nonnull final String stripeEventId,
                                @Nonnull final String eventType,
                                @Nonnull final BillingWebhookEventStatus status,
                                @Nullable final OffsetDateTime processedAt,
                                @Nullable final OffsetDateTime createdAt,
                                @Nonnull final String payloadJson) {
    this(null, stripeEventId, eventType, status, processedAt, createdAt, payloadJson);
  }

  public BillingWebhookEventDto(@Nullable final Long id,
                                @Nonnull final String stripeEventId,
                                @Nonnull final String eventType,
                                @Nonnull final BillingWebhookEventStatus status,
                                @Nullable final OffsetDateTime processedAt,
                                @Nullable final OffsetDateTime createdAt,
                                @Nonnull final String payloadJson) {
    setId(id);
    this.stripeEventId = stripeEventId;
    this.eventType = eventType;
    this.status = status;
    this.processedAt = processedAt;
    this.createdAt = createdAt;
    this.payloadJson = payloadJson;
  }

  @Nonnull
  public String getStripeEventId() {
    return stripeEventId;
  }

  public void setStripeEventId(@Nonnull final String stripeEventId) {
    this.stripeEventId = stripeEventId;
  }

  @Nonnull
  public String getEventType() {
    return eventType;
  }

  public void setEventType(@Nonnull final String eventType) {
    this.eventType = eventType;
  }

  @Nonnull
  public BillingWebhookEventStatus getStatus() {
    return status;
  }

  public void setStatus(@Nonnull final BillingWebhookEventStatus status) {
    this.status = status;
  }

  @Nullable
  public OffsetDateTime getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(@Nullable final OffsetDateTime processedAt) {
    this.processedAt = processedAt;
  }

  @Nullable
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Nonnull
  public String getPayloadJson() {
    return payloadJson;
  }

  public void setPayloadJson(@Nonnull final String payloadJson) {
    this.payloadJson = payloadJson;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof BillingWebhookEventDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}