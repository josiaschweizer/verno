package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

/**
 * Import configuration for Instructor entities.
 */
public class InstructorImportConfig implements ImportEntityConfig<InstructorDto> {

  @Nonnull private final VernoApplicationGate vernoApplicationGate;

  public InstructorImportConfig(@Nonnull final VernoApplicationGate vernoApplicationGate) {
    this.vernoApplicationGate = vernoApplicationGate;
  }

  @Nonnull
  @Override
  public List<DbField<InstructorDto>> getDbFields() {
    return List.of();
  }

  @Override
  public List<DbFieldTyped<InstructorDto, ?>> getTypedDbFields() {
    return List.of();
  }

  @Override
  public boolean performImport(@Nonnull final String fileToken,
                               @Nonnull final Map<String, String> mapping) {
    // TODO: Implement actual instructor import logic
    // Example: vernoApplicationGate.getService(IInstructorService.class).importFromCsv(fileToken, mapping);
    return true;
  }
}
