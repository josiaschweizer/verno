package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

public final class ParticipantCsvMapper extends AbstractCsvMapper<ParticipantDto> {

  @Nonnull
  @Override
  protected ParticipantDto newTarget() {
    return new ParticipantDto();
  }
}