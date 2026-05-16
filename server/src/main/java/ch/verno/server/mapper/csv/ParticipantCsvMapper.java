package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;

public final class ParticipantCsvMapper extends AbstractCsvMapper<ParticipantDto> {

  public ParticipantCsvMapper(@Nonnull final GlobalInterface globalInterface){
    super(globalInterface);
  }

  @Nonnull
  @Override
  protected ParticipantDto newTarget() {
    return new ParticipantDto();
  }
}