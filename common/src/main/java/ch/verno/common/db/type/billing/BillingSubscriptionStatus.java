package ch.verno.common.db.type.billing;

import jakarta.annotation.Nonnull;

public enum BillingSubscriptionStatus {
  INACTIVE,
  TRIAL,
  ACTIVE,
  PAST_DUE,
  CANCELED,
  BLOCKED,
  ;

  public boolean equalsFromString(@Nonnull final String value) {
    return this.name().equalsIgnoreCase(value);
  }
}