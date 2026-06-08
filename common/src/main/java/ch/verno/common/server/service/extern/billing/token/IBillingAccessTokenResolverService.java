package ch.verno.common.server.service.extern.billing.token;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import jakarta.annotation.Nonnull;

public interface IBillingAccessTokenResolverService {

  @Nonnull
  BillingAccessTokenDto resolveBillingAccessToken(@Nonnull String rawToken);

  @Nonnull
  BillingAccessTokenDto resolveBillingAccessToken(@Nonnull String rawToken,
                                                  @Nonnull BillingAccessTokenPurpose expectedPurpose);

  @Nonnull
  BillingAccessTokenDto resolveAndMarkBillingAccessTokenAsUsed(@Nonnull String rawToken);

  boolean isBillingAccessTokenValid(@Nonnull String rawToken);

}
