package ch.verno.common.type.billing;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

public enum BillingSubscriptionStatus {
  NONE(Publ.EMPTY_STRING, "shared.none"),
  INACTIVE("INACTIVE", "shared.inactive"),
  TRIAL("TRIAL", "shared.trial"),
  ACTIVE("ACTIVE", "shared.active"),
  PAST_DUE("PAST_DUE", "shared.past_due"),
  CANCELED("CANCELED", "shared.cancelled"),
  BLOCKED("BLOCKED", "shared.blocked"),
  ;

  @Nonnull private final String key;
  @Nonnull private final String translationKey;

  BillingSubscriptionStatus(@Nonnull final String key,
                            @Nonnull final String translationKey) {
    this.key = key;
    this.translationKey = translationKey;
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

  @Nonnull
  public String getTranslationKey() {
    return translationKey;
  }
}