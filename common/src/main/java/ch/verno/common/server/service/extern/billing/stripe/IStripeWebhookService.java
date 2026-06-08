package ch.verno.common.server.service.extern.billing.stripe;

import jakarta.annotation.Nonnull;

public interface IStripeWebhookService {

  void handleStripeWebhook(@Nonnull String payload,
                           @Nonnull String signatureHeader);

}
