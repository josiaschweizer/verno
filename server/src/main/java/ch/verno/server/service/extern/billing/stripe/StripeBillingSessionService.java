package ch.verno.server.service.extern.billing.stripe;

import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoSecrets;
import ch.verno.server.applicationproperties.BillingConfigProvider;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.env.EnvironmentVariableBo;
import ch.verno.server.service.extern.billing.TenantBillingService;
import ch.verno.server.service.extern.billing.token.BillingAccessTokenResolverService;
import com.stripe.exception.StripeException;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class StripeBillingSessionService {

  @Nonnull private final Lazy<EnvironmentVariableBo> envBo;
  @Nonnull private final Lazy<TenantBillingService> tenantBillingService;
  @Nonnull private final Lazy<BillingAccessTokenResolverService> tokenResolver;
  @Nonnull private final Lazy<BillingConfigProvider> billingConfigProvider;

  public StripeBillingSessionService(@Nonnull final ServerBean bean) {
    this.tenantBillingService = Lazy.of(() -> bean.get(TenantBillingService.class));
    this.tokenResolver = Lazy.of(() -> bean.get(BillingAccessTokenResolverService.class));
    this.billingConfigProvider = Lazy.of(() -> bean.get(BillingConfigProvider.class));
    this.envBo = Lazy.of(() -> bean.get(EnvironmentVariableBo.class));
  }

  @Nonnull
  public String startBillingSession(@Nonnull final String rawToken) {
    final var resolvedToken = tokenResolver.get().resolveBillingAccessToken(rawToken);
    final var tenantId = resolvedToken.getTenantId();
    final var userId = resolvedToken.getUserId();

    if (tenantId == null) {
      throw new IllegalArgumentException("Tenant ID is null - we cannot start a billing session without a tenant context!");
    }

    final var billingOptional = tenantBillingService.get().findOptionalByTenantId(tenantId);
    final var metaData = new SessionMetaDataDto(
            String.valueOf(tenantId),
            String.valueOf(userId),
            billingOptional.map(TenantBillingDto::getStripeCustomerId).orElse(null)
    );

    if (billingOptional.isEmpty() || billingOptional.get().getStripeCustomerId().isBlank()) {
      return createCheckoutSession(metaData);
    }

    return createPortalSession(billingOptional.get());
  }

  @Nonnull
  private String createCheckoutSession(@Nonnull final SessionMetaDataDto metaData) {
    final var config = billingConfigProvider.get();
    final var stripePriceId = envBo.get().getEnv(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE);

    try {
      final var session = CheckoutSessionUtil.createSession(
              config.getCheckoutSuccessUrl(),
              config.getCheckoutCancelUrl(),
              stripePriceId,
              metaData
      );

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new IllegalStateException("Stripe checkout session url is missing");
      }

      return url;
    } catch (final StripeException exception) {
      throw new IllegalStateException("Failed to create Stripe checkout session", exception);
    }
  }

  @Nonnull
  private String createPortalSession(@Nonnull final TenantBillingDto billingDto) {
    if (billingDto.getStripeCustomerId().isBlank()) {
      throw new IllegalStateException("Stripe customer id must not be blank for portal session");
    }

    try {
      final var session = PortalSessionUtil.createPortalSession(
              billingDto,
              billingConfigProvider.get().getPortalReturnUrl()
      );

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new IllegalStateException("Stripe portal session url is missing");
      }

      return url;
    } catch (final StripeException exception) {
      throw new IllegalStateException("Failed to create Stripe billing portal session", exception);
    }
  }
}