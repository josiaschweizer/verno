package ch.verno.server.io.importing.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public record DbField<T>(
        @Nonnull String key,
        @Nonnull String label,
        @Nonnull BiConsumer<T, String> setter,
        boolean required,
        @Nullable Function<String, String> normalizer
) {

  public DbField(@Nonnull final String key,
                 @Nonnull final String label,
                 @Nonnull final BiConsumer<T, String> setter,
                 final boolean required) {
    this(key, label, setter, required, null);
  }

  @Nonnull
  public String normalizeValue(@Nonnull final String value) {
    if (normalizer == null) {
      return value;
    }
    return normalizer.apply(value);
  }
}