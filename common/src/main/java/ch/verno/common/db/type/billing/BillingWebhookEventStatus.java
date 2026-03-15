package ch.verno.common.db.type.billing;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public enum BillingWebhookEventStatus {
  NONE(Publ.EMPTY_STRING),
  RECEIVED("RECEIVED"),
  PROCESSED("PROCESSED"),
  FAILED("FAILED"),
  ;

  @Nonnull private final String key;

  BillingWebhookEventStatus(@Nonnull final String key) {
    this.key = key;
  }

  @Nonnull
  public static BillingWebhookEventStatus fromKey(@Nonnull final String key) {
    if (key.isBlank()) {
      return NONE;
    }

    for (final var status : BillingWebhookEventStatus.values()) {
      if (status.key.equals(key)) {
        return status;
      }
    }

    return NONE;
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}