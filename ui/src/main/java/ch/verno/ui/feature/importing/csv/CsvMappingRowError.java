package ch.verno.ui.feature.importing.csv;

import jakarta.annotation.Nonnull;

public record CsvMappingRowError(int rowIndex, @Nonnull String message) {}