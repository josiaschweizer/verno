package ch.verno.ui.feature.importing.csv;

import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public record CsvMappingResult<T extends BaseDto<?>>(
        @Nonnull List<T> saveables,
        @Nonnull List<CsvMappingRowError> errors
) {
}