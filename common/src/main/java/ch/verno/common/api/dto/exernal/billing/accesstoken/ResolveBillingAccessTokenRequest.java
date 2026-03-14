package ch.verno.common.api.dto.exernal.billing.accesstoken;

import jakarta.annotation.Nonnull;

public record ResolveBillingAccessTokenRequest(@Nonnull String token) {
}
