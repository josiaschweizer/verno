package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.service.extern.billing.token.IBillingAccessLinkService;
import ch.verno.common.db.service.extern.billing.token.IBillingAccessTokenGeneratorService;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.properties.ApplicationPropertiesGate;
import ch.verno.publ.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class BillingAccessLinkService implements IBillingAccessLinkService {

  @Nonnull private final IBillingAccessTokenGeneratorService billingAccessTokenGeneratorService;
  @Nonnull private final String subscriptionOverviewUrl;

  public BillingAccessLinkService(@Nonnull final GlobalInterface globalInterface) {
    subscriptionOverviewUrl = globalInterface.getService(ApplicationPropertiesGate.class).getSubscriptionOverviewUrl();
    billingAccessTokenGeneratorService = globalInterface.getService(IBillingAccessTokenGeneratorService.class);
  }

  @Nonnull
  @Override
  public String createSubscriptionOverviewUrl(@Nonnull final Long tenantId,
                                              @Nonnull final Long userId) {
    final var generatedToken = billingAccessTokenGeneratorService.generateBillingAccessToken(
            tenantId,
            userId,
            BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD
    );

    return subscriptionOverviewUrl + ApiUrl.ENTRY_TOKEN + generatedToken.getRawToken();
  }

}
