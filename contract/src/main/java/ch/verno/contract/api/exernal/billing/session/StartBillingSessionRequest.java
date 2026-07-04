package ch.verno.contract.api.exernal.billing.session;

import jakarta.annotation.Nonnull;

public record StartBillingSessionRequest(@Nonnull String token) {
}
