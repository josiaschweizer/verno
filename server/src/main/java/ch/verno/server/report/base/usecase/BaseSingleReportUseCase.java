package ch.verno.server.report.base.usecase;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface BaseSingleReportUseCase<T extends BaseDto<?>> extends BaseReportUseCase {

  FileDto generate(@Nonnull final T dto);

  FileDto generate(@Nonnull final T dto,
                   @Nullable final Object... additionalData);

}
