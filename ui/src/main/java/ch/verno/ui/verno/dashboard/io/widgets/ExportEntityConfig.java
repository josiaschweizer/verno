package ch.verno.ui.verno.dashboard.io.widgets;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ExportEntityConfig<T> {

  @Nonnull
  List<CsvMapDto> getRows();

  @Nonnull
  String getFileName();

}
