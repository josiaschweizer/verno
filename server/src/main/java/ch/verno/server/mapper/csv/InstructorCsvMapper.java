package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;

public class InstructorCsvMapper extends AbstractCsvMapper<InstructorDto> {

  public InstructorCsvMapper(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface);
  }

  @Nonnull
  @Override
  protected InstructorDto newTarget() {
    return new InstructorDto();
  }

}
