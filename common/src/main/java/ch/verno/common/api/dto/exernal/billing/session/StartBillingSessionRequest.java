package ch.verno.common.api.dto.exernal.billing.session;

import jakarta.annotation.Nonnull;

public record StartBillingSessionRequest(@Nonnull String token) {
}
