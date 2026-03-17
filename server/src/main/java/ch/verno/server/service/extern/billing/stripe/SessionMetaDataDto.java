package ch.verno.server.service.extern.billing.stripe;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SessionMetaDataDto(@Nonnull String tenantId,
                                 @Nonnull String userId,
                                 @Nullable String customerId) {
}
