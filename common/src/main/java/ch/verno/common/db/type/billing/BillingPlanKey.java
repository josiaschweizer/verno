package ch.verno.common.db.type.billing;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoSecrets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public enum BillingPlanKey {
  FREE(Publ.EMPTY_STRING, "shared.free"),
  BASIC(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE, "shared.basic"),
  PRO(VernoSecrets.ENV_STRIPE_PRICE_ID_PRO_PACKAGE, "shared.pro"),
  ;

  @Nonnull private final String secretKey;
  @Nonnull private final String planTranslationKey;

  BillingPlanKey(@Nonnull final String secretKey,
                 @Nonnull final String planTranslationKey) {
    this.secretKey = secretKey;
    this.planTranslationKey = planTranslationKey;
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

  @Nonnull
  public String getPlanTranslationKey() {
    return planTranslationKey;
  }
}
