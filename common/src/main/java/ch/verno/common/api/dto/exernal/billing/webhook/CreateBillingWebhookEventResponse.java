package ch.verno.common.api.dto.exernal.billing.webhook;

import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public record CreateBillingWebhookEventResponse(
        long id,
        @Nonnull String stripeEventId,
        @Nonnull String eventType,
        @Nonnull String status,
        OffsetDateTime processedAt) {
}
