package ch.verno.server.report.base.usecase;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.file.dto.FileDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface BaseListReportUseCase<T extends BaseDto> extends BaseReportUseCase {

  FileDto generate(@Nonnull List<T> dtos);

}
