package ch.verno.common.api.dto.exernal.billing.accesstoken;

import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public record ResolveBillingAccessTokenResponse(
        @Nonnull Long tenantId,
        @Nonnull Long userId,
        @Nonnull String purpose,
        OffsetDateTime expiresAt) {
}
