package ch.verno.common.io.importing;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents a relation field that can be imported from CSV.
 * The CSV value is interpreted as a code/key and resolved to an existing related entity.
 *
 * @param <T> The target entity type being imported
 * @param <R> The related entity type to resolve
 */
public record DbFieldRelation<T, R>(
        @Nonnull String key,
        @Nonnull String label,
        @Nonnull Function<String, R> resolver,
        @Nonnull BiConsumer<T, R> setter,
        boolean required
) {

  @Nullable
  public R resolve(@Nullable final String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    return resolver.apply(value.trim());
  }
}