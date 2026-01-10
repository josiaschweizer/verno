package ch.verno.server.report.participant;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.report.dto.ParticipantReportDto;
import jakarta.annotation.Nonnull;

public class ParticipantReportMapper {

  private ParticipantReportMapper() {
  }

  @Nonnull
  public static ParticipantReportDto map(@Nonnull final ParticipantDto participant) {
    return new ParticipantReportDto(
            participant.getFirstName(),
            participant.getLastName(),
            participant.getBirthdate(),
            participant.getGenderAsString(),
            participant.getParentOne().displayName(),
            participant.getParentTwo().displayName(),
            participant.getAddress().getFullAddressAsString(),
            participant.getEmail(),
            participant.getPhone().toString(),
            participant.getCourseLevelsAsString(),
            participant.getNote()
    );
  }

}
