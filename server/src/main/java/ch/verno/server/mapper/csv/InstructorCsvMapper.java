package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.InstructorDto;
import jakarta.annotation.Nonnull;

public class InstructorCsvMapper extends AbstractCsvMapper<InstructorDto> {

  @Nonnull
  @Override
  protected InstructorDto newTarget() {
    return new InstructorDto();
  }

}
