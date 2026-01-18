package ch.verno.server.mapper.csv;

import jakarta.annotation.Nonnull;

public record CsvMappingRowError(int rowIndex, @Nonnull String message) {}