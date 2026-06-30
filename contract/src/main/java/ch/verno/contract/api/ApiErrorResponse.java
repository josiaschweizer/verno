package ch.verno.contract.api;

import jakarta.annotation.Nonnull;

public record ApiErrorResponse(@Nonnull String code,
                               @Nonnull String message) {
}
