package ch.verno.server.report.base.usecase;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface BaseListReportUseCase<T extends BaseDto<?>> extends BaseReportUseCase {

  @Nonnull
  FileDto generate(@Nonnull List<T> dtos);

}
