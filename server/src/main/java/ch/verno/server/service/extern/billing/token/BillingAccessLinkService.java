package ch.verno.server.service.extern.billing.token;

import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.lib.Lazy;
import ch.verno.server.application.properties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.extern.billing.TenantBillingService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class BillingAccessLinkService {

  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<BillingAccessTokenGeneratorService> tokenGeneratorService;
  @Nonnull private final String subscriptionOverviewUrl;

  public BillingAccessLinkService(@Nonnull final ServerBean bean) {
    this.tenantBillingService = Lazy.of(() -> bean.get(TenantBillingService.class));
    this.tokenGeneratorService = Lazy.of(() -> bean.get(BillingAccessTokenGeneratorService.class));
    this.subscriptionOverviewUrl = bean.get(BillingConfigProvider.class).getSubscriptionOverviewUrl();
  }

  @Nonnull
  public String createSubscriptionUrlForCheckout(@Nonnull final Long tenantId,
                                                 @Nonnull final Long userId) {
    final var tenantBilling = tenantBillingService.get().findOptionalByTenantId(tenantId);
    final var tokenPurpose = tenantBilling.isPresent()
            ? BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD
            : BillingAccessTokenPurpose.START_CHECKOUT;

    return createSubscriptionUrl(tenantId, userId, tokenPurpose);
  }

  @Nonnull
  public String createSubscriptionUrl(@Nonnull final Long tenantId,
                                      @Nonnull final Long userId,
                                      @Nonnull final BillingAccessTokenPurpose tokenPurpose) {
    final var generatedToken = tokenGeneratorService.get()
            .generateBillingAccessToken(tenantId, userId, tokenPurpose);

    return UrlUtil.buildUrl(
            subscriptionOverviewUrl,
            ApiUrl.ENTRY_TOKEN + generatedToken.rawToken()
    );
  }
}