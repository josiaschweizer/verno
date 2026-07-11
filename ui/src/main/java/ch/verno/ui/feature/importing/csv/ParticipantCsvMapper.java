package ch.verno.ui.feature.importing.csv;

import ch.verno.contract.dto.table.participant.ParticipantDto;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public final class ParticipantCsvMapper extends AbstractCsvMapper<ParticipantDto> {

  @Inject
  public ParticipantCsvMapper(@Nonnull final Injector injector) {
    super(injector);
  }

  @Nonnull
  @Override
  protected ParticipantDto newTarget() {
    return ParticipantDto.empty();
  }
}