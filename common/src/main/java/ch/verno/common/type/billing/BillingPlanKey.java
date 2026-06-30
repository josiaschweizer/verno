package ch.verno.common.type.billing;

import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoSecrets;
import jakarta.annotation.Nonnull;

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
    return getBillingLicenceOptions().contains(option);
  }
}
