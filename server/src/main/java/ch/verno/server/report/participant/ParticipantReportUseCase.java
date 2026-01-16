package ch.verno.server.report.participant;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.report.ReportDto;
import ch.verno.report.dto.ParticipantListReportDto;
import ch.verno.report.dto.ParticipantReportDto;
import ch.verno.server.report.base.usecase.BaseListReportUseCase;
import ch.verno.server.service.ParticipantService;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantReportUseCase implements BaseListReportUseCase<ParticipantDto> {

  @Nonnull private final ParticipantService participantService;
  @Nonnull private final ParticipantReportRenderer reportRenderer;

  public ParticipantReportUseCase(@Nonnull final ParticipantService participantService,
                                  @Nonnull final ParticipantReportRenderer participantReportRenderer) {
    this.participantService = participantService;
    this.reportRenderer = participantReportRenderer;
  }

  public ReportDto generate() {
    final var participants = participantService.getAllParticipants();
    return generate(participants);
  }

  @Override
  public ReportDto generate(@NonNull final List<ParticipantDto> dtos) {
    final var dtoList = new ArrayList<ParticipantReportDto>();
    for (final var dto : dtos) {
      dtoList.add(ParticipantReportMapper.map(dto));
    }

    final var reportData = new ParticipantListReportDto(dtoList);

    final var filename = "participant_list_report.pdf";
    final var pdfBytes = reportRenderer.renderReportPdf(reportData);

    return new ReportDto(filename, pdfBytes);
  }
}
