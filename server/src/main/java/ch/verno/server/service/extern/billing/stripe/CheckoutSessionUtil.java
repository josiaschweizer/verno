package ch.verno.server.service.extern.billing.stripe;

import ch.verno.publ.VernoConstants;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.Nonnull;

class CheckoutSessionUtil {

  @Nonnull
  static Session createSession(@Nonnull final String checkoutSuccessUrl,
                               @Nonnull final String checkoutCancelUrl,
                               @Nonnull final String stripePriceId,
                               @Nonnull final SessionMetaDataDto metaData) throws StripeException {
    final var params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setSuccessUrl(checkoutSuccessUrl)
            .setCancelUrl(checkoutCancelUrl)
            .putMetadata(VernoConstants.SESSION_TENANT_ID, metaData.tenantId())
            .putMetadata(VernoConstants.SESSION_USER_ID, metaData.userId())
            .putMetadata(VernoConstants.SESSION_STRIPE_PRICE_ID, stripePriceId)
            .setClientReferenceId(metaData.tenantId())
            .addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPrice(stripePriceId)
                            .setQuantity(1L)
                            .build()
            )
            .build();

    return Session.create(params);
  }
}
