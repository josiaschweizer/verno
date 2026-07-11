package ch.verno.contract.api.exernal.billing.accesstoken;

import jakarta.annotation.Nonnull;

public record ResolveBillingAccessTokenRequest(@Nonnull String token) {
}
