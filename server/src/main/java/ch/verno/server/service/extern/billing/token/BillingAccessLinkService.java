package ch.verno.server.service.extern.billing.token;

import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.service.extern.billing.token.IBillingAccessLinkService;
import ch.verno.common.db.service.extern.billing.token.IBillingAccessTokenGeneratorService;
import ch.verno.common.db.type.billing.BillingAccessTokenPurpose;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.properties.configprovider.VernoBillingConfigProvider;
import ch.verno.publ.ApiUrl;
import ch.verno.server.service.extern.billing.TenantBillingService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class BillingAccessLinkService implements IBillingAccessLinkService {

  @Nonnull private final ITenantBillingService tenantBillingService;
  @Nonnull private final IBillingAccessTokenGeneratorService billingAccessTokenGeneratorService;

  @Nonnull private final String subscriptionOverviewUrl;

  public BillingAccessLinkService(@Nonnull final GlobalInterface globalInterface) {
    tenantBillingService = globalInterface.getService(TenantBillingService.class);
    billingAccessTokenGeneratorService = globalInterface.getService(IBillingAccessTokenGeneratorService.class);

    subscriptionOverviewUrl = globalInterface.getService(VernoBillingConfigProvider.class).getSubscriptionOverviewUrl();
  }

  @Nonnull
  @Override
  public String createSubscriptionUrlForCheckout(@Nonnull final Long tenantId, @Nonnull final Long userId) {
    final var tenantBilling = tenantBillingService.getOptionalTenantBillingByTenantId(tenantId);
    final var tokenPurpose = tenantBilling.isPresent() ?
            BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD :
            BillingAccessTokenPurpose.START_CHECKOUT;

    final var generatedToken = billingAccessTokenGeneratorService.generateBillingAccessToken(
            tenantId,
            userId,
            tokenPurpose
    );

    return UrlUtil.buildUrl(subscriptionOverviewUrl, ApiUrl.ENTRY_TOKEN + generatedToken.getRawToken());
  }

  @Nonnull
  public String createSubscriptionUrl(@Nonnull final Long tenantId,
                                      @Nonnull final Long userId,
                                      @Nonnull final BillingAccessTokenPurpose tokenPurpose) {
    final var generatedToken = billingAccessTokenGeneratorService.generateBillingAccessToken(
            tenantId,
            userId,
            tokenPurpose
    );

    return subscriptionOverviewUrl + ApiUrl.ENTRY_TOKEN + generatedToken.getRawToken();
  }
}
