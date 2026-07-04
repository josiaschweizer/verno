package ch.verno.contract.api.exernal.billing.tenant;

import jakarta.annotation.Nonnull;

public record CreateTenantBillingRequest(
        long tenantId,
        @Nonnull String planKey,
        @Nonnull String subscriptionStatus,
        @Nonnull String paymentStatus,
        boolean hasValidPaymentMethod) {
}
