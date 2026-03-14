package ch.verno.common.api.dto.exernal.billing.accesstoken;

import jakarta.annotation.Nonnull;

import java.time.OffsetDateTime;

public record CreateBillingAccessTokenResponse(
        long id,
        long tenantId,
        long userId,
        @Nonnull String purpose,
        @Nonnull OffsetDateTime expiresAt,
        OffsetDateTime usedAt) {
}
