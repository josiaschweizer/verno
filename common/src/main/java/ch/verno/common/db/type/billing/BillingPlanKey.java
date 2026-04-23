package ch.verno.common.db.type.billing;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.New;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoSecrets;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public enum BillingPlanKey {
  FREE(Publ.EMPTY_STRING, New.arrayList(), "shared.free"),
  BASIC(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE, New.arrayList(), "shared.basic"),
  PRO(VernoSecrets.ENV_STRIPE_PRICE_ID_PRO_PACKAGE, New.arrayList(BillingLicenceOption.REPORT), "shared.pro"),
  ;

  @Nonnull private final String secretKey;
  @Nonnull private final String planTranslationKey;
  @Nonnull private final List<BillingLicenceOption> billingLicenceOptions;

  BillingPlanKey(@Nonnull final String secretKey,
                 @Nonnull final List<BillingLicenceOption> billingLicenceOptions,
                 @Nonnull final String planTranslationKey) {
    this.secretKey = secretKey;
    this.planTranslationKey = planTranslationKey;
    this.billingLicenceOptions = billingLicenceOptions;
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

  @Nonnull
  public List<BillingLicenceOption> getBillingLicenceOptions() {
    return billingLicenceOptions;
  }

  public boolean isLicenced(@Nonnull final BillingLicenceOption option) {
    if (this.getBillingLicenceOptions().contains(option)) {
      return true;
    }

    return false;
  }
}
