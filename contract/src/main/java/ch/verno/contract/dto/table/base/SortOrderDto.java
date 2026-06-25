package ch.verno.contract.dto.table.base;

import jakarta.annotation.Nonnull;

public record SortOrderDto(@Nonnull String property, boolean ascending) {
}