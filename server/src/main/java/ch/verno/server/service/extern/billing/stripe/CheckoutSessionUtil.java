package ch.verno.server.service.extern.billing.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.Nonnull;

class CheckoutSessionUtil {

  @Nonnull
  static Session createSession(@Nonnull final String checkoutSuccessUrl,
                               @Nonnull final String checkoutCancelUrl,
                               @Nonnull final String stripePriceId) throws StripeException {
    final var params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
            .setSuccessUrl(checkoutSuccessUrl)
            .setCancelUrl(checkoutCancelUrl)
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
