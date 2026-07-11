package ch.verno.contract.api.exernal.billing.webhook;

import jakarta.annotation.Nonnull;

public record CreateBillingWebhookEventRequest(
        @Nonnull String stripeEventId,
        @Nonnull String eventType,
        @Nonnull String status,
        String payloadJson) {
}
