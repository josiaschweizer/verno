package ch.verno.server.service.extern.billing.stripe;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import com.stripe.exception.StripeException;
import com.stripe.model.billingportal.Session;
import com.stripe.param.billingportal.SessionCreateParams;
import jakarta.annotation.Nonnull;

class PortalSessionUtil {

  @Nonnull
  static Session createPortalSession(@Nonnull final TenantBillingDto billingDto,
                                     @Nonnull final String portalReturnUrl) throws StripeException {
    final var params = SessionCreateParams.builder()
            .setCustomer(billingDto.getStripeCustomerId())
            .setReturnUrl(portalReturnUrl)
            .build();

    return Session.create(params);
  }
}
