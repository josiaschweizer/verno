package ch.verno.common.api.util;

import jakarta.annotation.Nonnull;

public record ApiErrorResponse(@Nonnull String code,
                               @Nonnull String message) {
}
