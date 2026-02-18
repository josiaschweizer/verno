package ch.verno.common.api.dto.internal.file.temp;

import jakarta.annotation.Nonnull;

import java.util.Map;

public record CsvMapDto(@Nonnull Map<String, String> row) {
}
