package ch.verno.ui.verno.dashboard.report;

import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.report.VABaseReport;
import jakarta.annotation.Nonnull;

import java.util.List;

public class ParticipantReport extends VABaseReport<ReportParticipant> {

  @Nonnull
  private final ParticipantService participantService;

  public ParticipantReport(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;

    setTitle("Participants Report");
    setSubtitle("Total Participants: " + participantService.getAllParticipants().size());

    initUI();
  }

  @Nonnull
  @Override
  protected List<ReportParticipant> getDataList() {
    return participantService.getAllParticipants().stream()
            .map(participant -> new ReportParticipant(
                    participant.getFirstName(),
                    participant.getLastName(),
                    participant.getEmail()
            ))
            .toList();
  }

  @Nonnull
  @Override
  protected Class<ReportParticipant> getType() {
    return ReportParticipant.class;
  }

  @Nonnull
  @Override
  protected String[] getColumnProperties() {
    return new String[]{"firstName", "lastName", "email"};
  }
}
