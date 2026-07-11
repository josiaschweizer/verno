package ch.verno.server.report.base.usecase;


import ch.verno.contract.dto.file.temp.FileDto;
import jakarta.annotation.Nonnull;

public interface BaseReportUseCase {

  @Nonnull
  FileDto generate();

}
