package ch.verno.contract.api.exernal.billing.tenant;

import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public record CreateTenantBillingResponse (
        long id,
        long tenantId,
        @Nonnull String planKey,
        @Nonnull String subscriptionStatus,
        @Nonnull String paymentStatus,
        boolean hasValidPaymentMethod,
        OffsetDateTime currentPeriodEnd,
        OffsetDateTime graceUntil){
}
