package ch.verno.server.io.importing.dto;

import jakarta.annotation.Nonnull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record DbFieldTyped<T, V>(
        @Nonnull String key,
        @Nonnull String label,
        @Nonnull Function<String, V> parser,
        @Nonnull BiConsumer<T, V> setter,
        boolean required
) {
}