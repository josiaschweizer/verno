package ch.verno.common.api.dto.exernal.billing.webhook;

import javax.annotation.Nonnull;

public record CreateBillingWebhookEventRequest(
        @Nonnull String stripeEventId,
        @Nonnull String eventType,
        @Nonnull String status,
        String payloadJson) {
}
