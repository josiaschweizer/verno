package ch.verno.common.lib.mail.placeholder;

import jakarta.annotation.Nonnull;

import java.util.function.Function;

public record PlaceholderValue<T>(
        @Nonnull Placeholder placeholder,
        @Nonnull Function<T, String> valueFunction) {
}
