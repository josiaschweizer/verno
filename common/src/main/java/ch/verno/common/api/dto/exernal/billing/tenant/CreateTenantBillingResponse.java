package ch.verno.common.api.dto.exernal.billing.tenant;

import javax.annotation.Nonnull;
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
