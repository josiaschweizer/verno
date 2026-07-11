package ch.verno.server.bo.billing.stripe;

import ch.verno.contract.dto.table.billing.TenantBillingDto;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import jakarta.annotation.Nonnull;

public class PortalSessionUtil {

  @Nonnull
  public static Session createPortalSession(@Nonnull final TenantBillingDto tenantBilling,
                                            @Nonnull final String portalReturnUrl) throws StripeException {
    final var params = SessionCreateParams.builder()
            .setCustomer(tenantBilling.getStripeCustomerId())
            .setReturnUrl(portalReturnUrl)
            .build();

    return Session.create(params);
  }
}
