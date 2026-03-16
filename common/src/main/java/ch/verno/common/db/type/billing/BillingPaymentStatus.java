package ch.verno.common.db.type.billing;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public enum BillingPaymentStatus {
  NONE(Publ.EMPTY_STRING, "shared.none"),
  UNPAID("UNPAID", "shared.unpaid"),
  PAID("PAID", "shared.paid"),
  FAILED("FAILED", "shared.failed"),
  REQUIRES_ACTION("REQUIRES_ACTION", "shared.requires_action"),
  ;

  @Nonnull private final String key;
  @Nonnull private final String translationKey;

  BillingPaymentStatus(@Nonnull final String key,
                       @Nonnull final String translationKey) {
    this.key = key;
    this.translationKey = translationKey;
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

  @Nonnull
  public String getTranslationKey() {
    return translationKey;
  }
}