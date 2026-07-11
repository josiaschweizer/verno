package ch.verno.server.bo.billing.accesslink;

import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.type.billing.BillingAccessTokenPurpose;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.lib.Lazy;
import ch.verno.server.application.properties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.billing.accesstoken.BillingTokenGeneratorBo;
import ch.verno.server.service.entity.billing.TenantBillingService;
import jakarta.annotation.Nonnull;

public class BillingAccessLinkBo {

  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<BillingTokenGeneratorBo> billingTokenGeneratorBo;

  @Nonnull private final String subscriptionOverviewUrl;

  protected BillingAccessLinkBo(@Nonnull final ServerBean serverBean) {
    this.tenantBillingService = Lazy.of(() -> serverBean.get(TenantBillingService.class));
    this.billingTokenGeneratorBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(BillingTokenGeneratorBo.class));

    this.subscriptionOverviewUrl = serverBean.get(BillingConfigProvider.class).getSubscriptionOverviewUrl();
  }

  public String createSubscriptionUrlForCheckout(@Nonnull final Long tenantId,
                                                 @Nonnull final Long userId) {
    final var tenantBilling = tenantBillingService.get().findOptionalByTenantId(tenantId);
    final var tokenPurpose = tenantBilling.isPresent() ?
            BillingAccessTokenPurpose.UPDATE_PAYMENT_METHOD :
            BillingAccessTokenPurpose.START_CHECKOUT;

    return createSubscriptionUrl(tenantId, userId, tokenPurpose);
  }

  @Nonnull
  public String createSubscriptionUrl(@Nonnull final Long tenantId,
                                      @Nonnull final Long userId,
                                      @Nonnull final BillingAccessTokenPurpose purpose) {
    final var token = billingTokenGeneratorBo.get().generateBillingAccessToken(tenantId, userId, purpose);
    return UrlUtil.buildUrl(subscriptionOverviewUrl, ApiUrl.ENTRY_TOKEN + token.rawToken());
  }

}
