package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public record CsvMappingResult<T extends BaseDto>(
        @Nonnull List<T> saveables,
        @Nonnull List<CsvMappingRowError> errors
) {
}