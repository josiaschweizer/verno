package ch.verno.common.file.dto;

import jakarta.annotation.Nonnull;

import java.util.Map;

public record CsvMapDto(@Nonnull Map<String, String> row) {
}
