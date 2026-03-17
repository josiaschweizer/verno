package ch.verno.db.entity.billing;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "billing_webhook_event", schema = "public")
public class BillingWebhookEventEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "stripe_event_id", nullable = false, unique = true)
  private String stripeEventId;

  @Nonnull
  @Column(name = "event_type", nullable = false)
  private String eventType;

  @Nonnull
  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "processed_at")
  private OffsetDateTime processedAt;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "payload_json", columnDefinition = "text")
  private String payloadJson;

  protected BillingWebhookEventEntity() {
    // JPA
  }

  public BillingWebhookEventEntity(@Nonnull final String stripeEventId,
                                   @Nonnull final String eventType,
                                   @Nonnull final String status,
                                   final String payloadJson) {
    this.stripeEventId = stripeEventId;
    this.eventType = eventType;
    this.status = status;
    this.payloadJson = payloadJson;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
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
  public String getStatus() {
    return status;
  }

  public void setStatus(@Nonnull final String status) {
    this.status = status;
  }

  public OffsetDateTime getProcessedAt() {
    return processedAt;
  }

  public void setProcessedAt(final OffsetDateTime processedAt) {
    this.processedAt = processedAt;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getPayloadJson() {
    return payloadJson;
  }

  public void setPayloadJson(final String payloadJson) {
    this.payloadJson = payloadJson;
  }
}