package ch.verno.server.report.base.usecase;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.api.dto.internal.file.temp.FileDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface BaseSingleReportUseCase<T extends BaseDto> extends BaseReportUseCase {

  FileDto generate(@Nonnull final T dto);

  FileDto generate(@Nonnull final T dto,
                   @Nullable final Object... additionalData);

}
