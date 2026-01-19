package ch.verno.ui.verno.dashboard.io.widgets;

import ch.verno.common.file.dto.CsvMapDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ExportEntityConfig<T> {

  @Nonnull
  List<CsvMapDto> getRows();

}
