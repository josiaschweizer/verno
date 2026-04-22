package ch.verno.ui.base.components.mapping;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record MappingRow(@Nonnull String csvColumn,
                         @Nullable String fieldKey) {
}