package ch.verno.server.type.billing;

import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.lib.Publ;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class BillingPlanResolver {

  /**
   * Resolves the billing plan by the subscription id which is stored as an env-property on the billing plan key
   * Returns the Free BillingPlanKey as fallback
   *
   * @param subscriptionId        the subscription id that is compared with the env variable value of the billing plan key
   * @param environmentVariableBo business object to resolve the env variable from the billing plan key
   * @return the billing plan key that has the same secret key value as the subscription id or a Free BillingPlanKey
   */
  @Nonnull
  public static BillingPlanKey resolve(@Nullable final String subscriptionId,
                                       @Nonnull final EnvironmentVariableBo environmentVariableBo) {
    if (subscriptionId == null || subscriptionId.isBlank()) {
      return BillingPlanKey.FREE;
    }

    for (final var value : BillingPlanKey.values()) {
      final var secret = value.getSecretKey();
      if (!secret.isBlank() && environmentVariableBo.getEnvOrDefault(secret, Publ.EMPTY_STRING).equals(subscriptionId)) {
        return value;
      }
    }

    return BillingPlanKey.FREE;
  }

}
