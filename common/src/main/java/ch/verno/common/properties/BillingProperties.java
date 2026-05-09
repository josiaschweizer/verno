package ch.verno.common.properties;

import ch.verno.common.db.type.billing.BillingLicenceOption;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface BillingProperties {

  boolean isOptionLicenced(@Nonnull BillingLicenceOption option);

  @Nonnull
  List<BillingLicenceOption> getTenantLicenceOptions();

}
