package ch.verno.server.report.base.usecase;

import ch.verno.common.api.dto.internal.file.temp.FileDto;

public interface BaseReportUseCase {

  FileDto generate();

}
