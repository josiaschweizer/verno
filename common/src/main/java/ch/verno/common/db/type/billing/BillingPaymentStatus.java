package ch.verno.common.db.type.billing;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public enum BillingPaymentStatus {
  NONE(Publ.EMPTY_STRING),
  UNPAID("UNPAID"),
  PAID("PAID"),
  FAILED("FAILED"),
  REQUIRES_ACTION("REQUIRES_ACTION"),
  ;

  @Nonnull private final String key;

  BillingPaymentStatus(@Nonnull final String key) {
    this.key = key;
  }

  public static BillingPaymentStatus fromKey(@Nonnull final String key) {
    if (key.isBlank()) {
      return NONE;
    }

    for (final var billingPaymentStatus : BillingPaymentStatus.values()) {
      if (billingPaymentStatus.getKey().equalsIgnoreCase(key)) {
        return billingPaymentStatus;
      }
    }

    return NONE;
  }


  @Nonnull
  public String getKey() {
    return key;
  }
}