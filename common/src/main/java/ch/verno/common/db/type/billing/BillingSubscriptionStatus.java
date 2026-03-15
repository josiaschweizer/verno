package ch.verno.common.db.type.billing;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public enum BillingSubscriptionStatus {
  NONE(Publ.EMPTY_STRING),
  INACTIVE("INACTIVE"),
  TRIAL("TRIAL"),
  ACTIVE("ACTIVE"),
  PAST_DUE("PAST_DUE"),
  CANCELED("CANCELED"),
  BLOCKED("BLOCKED"),
  ;

  @Nonnull private final String key;

  BillingSubscriptionStatus(@Nonnull String key) {
    this.key = key;
  }

  public static BillingSubscriptionStatus fromKey(@Nonnull final String key) {
    if (key.isBlank()) {
      return NONE;
    }

    for (final var subscriptionStatus : BillingSubscriptionStatus.values()) {
      if (subscriptionStatus.getKey().equals(key)) {
        return subscriptionStatus;
      }
    }

    return NONE;
  }

  @Nonnull
  public static BillingSubscriptionStatus fromStripeStatus(@Nonnull final String stripeStatus) {
    return switch (stripeStatus) {
      case "trialing" -> BillingSubscriptionStatus.TRIAL;
      case "active" -> BillingSubscriptionStatus.ACTIVE;
      case "past_due" -> BillingSubscriptionStatus.PAST_DUE;
      case "canceled" -> BillingSubscriptionStatus.CANCELED;
      case "unpaid", "incomplete", "incomplete_expired" -> BillingSubscriptionStatus.INACTIVE;
      default -> BillingSubscriptionStatus.NONE;
    };
  }

  @Nonnull
  public String getKey() {
    return key;
  }
}