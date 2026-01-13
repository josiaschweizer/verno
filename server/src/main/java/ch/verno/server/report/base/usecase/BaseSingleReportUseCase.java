package ch.verno.server.report.base.usecase;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.report.ReportDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface BaseSingleReportUseCase<T extends BaseDto> extends BaseReportUseCase {

  ReportDto generate(@Nonnull final T dto);

  ReportDto generate(@Nonnull final T dto,
                     @Nullable final Object... additionalData);

}
