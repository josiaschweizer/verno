package ch.verno.server.service.extern.billing.stripe;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.service.extern.billing.stripe.IStripeBillingSessionService;
import ch.verno.common.db.service.extern.billing.token.IBillingAccessTokenResolverService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.properties.ApplicationPropertiesGate;
import ch.verno.publ.VernoSecrets;
import com.stripe.exception.StripeException;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class StripeBillingSessionService implements IStripeBillingSessionService {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ITenantBillingService tenantBillingService;
  @Nonnull private final IBillingAccessTokenResolverService tokenResolver;

  public StripeBillingSessionService(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    tenantBillingService = globalInterface.getService(ITenantBillingService.class);
    tokenResolver = globalInterface.getService(IBillingAccessTokenResolverService.class);
  }

  @Nonnull
  public String startBillingSession(@Nonnull final String rawToken) {
    final var resolvedToken = tokenResolver.resolveBillingAccessToken(rawToken);
    final var tenantId = resolvedToken.getTenantId();
    final var userId = resolvedToken.getUserId();

    if (tenantId == null) {
      throw new IllegalArgumentException("Tenant ID is null - we cannot start a billing session without a tenant context!");
    }

    final var billingOptional = tenantBillingService.getOptionalTenantBillingByTenantId(tenantId);

    final var dto = new SessionMetaDataDto(
            String.valueOf(tenantId),
            String.valueOf(userId)
    );

    if (billingOptional.isEmpty()) {
      return createCheckoutSession(dto);
    }

    final var billing = billingOptional.get();
    if (billing.getStripeCustomerId().isBlank()) {
      return createCheckoutSession(dto);
    } else {
      return createPortalSession(billing);
    }
  }

  @Nonnull
  private String createCheckoutSession(@Nonnull final SessionMetaDataDto dto) {
    final var applicationProperties = globalInterface.getService(ApplicationPropertiesGate.class);
    final var checkoutSuccessUrl = applicationProperties.getCheckoutSuccessUrl();
    final var checkoutCancelUrl = applicationProperties.getCheckoutCancelUrl();
    final var stripePriceId = globalInterface.getEnvProperties().getEnv(VernoSecrets.ENV_STRIPE_PRICE_ID_BASIC_PACKAGE); //todo updaten mit user selection -> verno-99

    try {
      final var session = CheckoutSessionUtil.createSession(
              checkoutSuccessUrl,
              checkoutCancelUrl,
              stripePriceId,
              dto
      );

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new IllegalStateException("Stripe checkout session url is missing");
      }

      return url;
    } catch (StripeException exception) {
      throw new IllegalStateException("Failed to create Stripe checkout session", exception);
    }
  }

  @Nonnull
  private String createPortalSession(@Nonnull final TenantBillingDto billingDto) {
    if (billingDto.getStripeCustomerId().isBlank()) {
      throw new IllegalStateException("Stripe customer id must not be blank for portal session");
    }

    final var applicationProperties = globalInterface.getService(ApplicationPropertiesGate.class);
    final var portalReturnUrl = applicationProperties.getPortalReturnUrl();

    try {
      final var session = PortalSessionUtil.createPortalSession(
              billingDto,
              portalReturnUrl
      );

      final var url = session.getUrl();
      if (url == null || url.isBlank()) {
        throw new IllegalStateException("Stripe portal session url is missing");
      }

      return url;
    } catch (StripeException exception) {
      throw new IllegalStateException("Failed to create Stripe billing portal session", exception);
    }
  }
}
