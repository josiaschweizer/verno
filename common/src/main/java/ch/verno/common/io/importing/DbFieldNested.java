package ch.verno.common.io.importing;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Represents a nested entity field that can be imported from CSV.
 * This allows importing related entities (e.g., Address, Parent) along with the main entity.
 *
 * @param <T> The parent entity type (e.g., ParticipantDto)
 * @param <N> The nested entity type (e.g., AddressDto, ParentDto)
 */
public record DbFieldNested<T, N>(
        @Nonnull String prefix,
        @Nonnull String label,
        @Nonnull Supplier<N> nestedFactory,
        @Nonnull BiConsumer<T, N> setter,
        @Nonnull List<DbField<N>> nestedStringFields,
        @Nonnull List<DbFieldTyped<N, ?>> nestedTypedFields,
        boolean required
) {
}
