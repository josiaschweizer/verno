package ch.verno.server.bo.billing.stripe;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoSecrets;
import ch.verno.lib.exception.stripe.StripeSessionException;
import ch.verno.server.application.properties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.billing.accesstoken.BillingTokenResolverBo;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import ch.verno.server.service.entity.billing.TenantBillingService;
import com.stripe.exception.StripeException;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class StripeBo {

  @Nonnull private final Lazy<BillingTokenResolverBo> billingResolverBo;
  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;

  @Nonnull private final Lazy<EnvironmentVariableBo> environmentVariableBo;
  @Nonnull private final Lazy<BillingConfigProvider> billingConfigProvider;

  protected StripeBo(@Nonnull final ServerBean serverBean) {
    this.billingResolverBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(BillingTokenResolverBo.class));
    this.tenantBillingService = Lazy.of(() -> serverBean.get(TenantBillingService.class));

    this.environmentVariableBo = Lazy.of(() -> BoFactory.getInstance(serverBean).getEmptyConstructor(EnvironmentVariableBo.class));
    this.billingConfigProvider = Lazy.of(() -> serverBean.get(BillingConfigProvider.class));
  }

  @Nonnull
  public String startBillingSession(@Nonnull final String rawToken) {
    final var resolvedToken = billingResolverBo.get().resolveBillingAccessToken(rawToken);
    final var userId = resolvedToken.getUserId();
    final var tenantId = Optional.ofNullable(resolvedToken.getTenantId()).orElseThrow(() -> new IllegalArgumentException("Tenant ID is null - we cannot start a billing session without a tenant context!"));
    TenantContext.set(tenantId);

    final var billingOptional = tenantBillingService.get().findOptionalByTenantId(tenantId);

    final var dto = new SessionMetaDataDto(
            String.valueOf(tenantId),
            String.valueOf(userId),
            billingOptional.map(TenantBillingDto::getStripeSubscriptionId).orElse(null)
    );

    if (billingOptional.isEmpty() || billingOptional.get().getStripeCustomerId().isBlank()) {
      return createCheckoutSession(dto);
    }

    return createPortalSession(billingOptional.get());
  }

  @Nonnull
  private String createCheckoutSession(@Nonnull final SessionMetaDataDto sessionMetaData) {
    final var stripePriceId = environmentVariableBo.get().getEnv(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE); //todo update with user selection -> verno-99
    final var checkoutCancelUrl = billingConfigProvider.get().getCheckoutCancelUrl();
    final var checkoutSuccessUrl = billingConfigProvider.get().getCheckoutSuccessUrl();

    try {
      final var session = CheckoutSessionUtil.createSession(
              checkoutSuccessUrl,
              checkoutCancelUrl,
              stripePriceId,
              sessionMetaData
      );

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new StripeSessionException("Missing checkout URL - checkout url cannot be empty");
      }

      return url;
    } catch (StripeException e) {
      throw new StripeSessionException(e);
    }
  }

  @Nonnull
  private String createPortalSession(@Nonnull final TenantBillingDto tenantBilling) {
    if (tenantBilling.getStripeCustomerId().isBlank()) {
      throw new IllegalStateException("Stripe customer id must not be blank for portal session");
    }

    final var portalReturnUrl = billingConfigProvider.get().getPortalReturnUrl();

    try {
      final var session = PortalSessionUtil.createPortalSession(tenantBilling, portalReturnUrl);

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new IllegalStateException("Missing portal URL - portal url cannot be empty");
      }

      return url;
    } catch (StripeException e) {
      throw new StripeSessionException(e);
    }
  }
}
