package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.file.dto.CsvMapDto;
import ch.verno.common.gate.GlobalGate;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import jakarta.annotation.Nonnull;

import java.util.List;

public class InstructorExportConfig implements ExportEntityConfig<InstructorDto> {

  @Nonnull private final GlobalGate globalGate;

  public InstructorExportConfig(@Nonnull final GlobalGate globalGate) {
    this.globalGate = globalGate;
  }

  @Nonnull
  @Override
  public List<CsvMapDto> getRows() {
    return List.of();
  }
}
