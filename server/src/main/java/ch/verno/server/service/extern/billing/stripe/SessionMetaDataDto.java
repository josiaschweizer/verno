package ch.verno.server.service.extern.billing.stripe;

import jakarta.annotation.Nonnull;

public record SessionMetaDataDto(@Nonnull String tenantId,
                                 @Nonnull String userId) {
}
