package ch.verno.common.db.type.billing;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoSecrets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public enum BillingPlanKey {
  FREE(Publ.EMPTY_STRING),
  BASIC(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE),
  PRO(VernoSecrets.ENV_STRIPE_PRICE_ID_PRO_PACKAGE),
  ;

  @Nonnull private final String secretKey;

  BillingPlanKey(@Nonnull final String secretKey) {
    this.secretKey = secretKey;
  }

  @Nonnull
  public static BillingPlanKey resolvePlan(@Nullable final String subscriptionId,
                                           @Nonnull final GlobalInterface globalInterface) {
    if (subscriptionId == null || subscriptionId.isBlank()) {
      return FREE;
    }

    for (final var value : BillingPlanKey.values()) {
      if (!value.getSecretKey().isBlank() &&
              globalInterface.getEnvProperties().getEnv(value.getSecretKey()).equals(subscriptionId)) {
        return value;
      }
    }

    return FREE;
  }

  @Nonnull
  public String getSecretKey() {
    return secretKey;
  }
}
