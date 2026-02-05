package ch.verno.server.report.participant;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.file.dto.FileDto;
import ch.verno.report.dto.ParticipantListReportDto;
import ch.verno.report.dto.ParticipantReportDto;
import ch.verno.server.report.base.usecase.BaseListReportUseCase;
import ch.verno.server.service.intern.ParticipantService;
import jakarta.annotation.Nonnull;
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

  public FileDto generate() {
    final var participants = participantService.getAllParticipants();
    return generate(participants);
  }

  @Override
  public FileDto generate(@Nonnull final List<ParticipantDto> dtos) {
    final var dtoList = new ArrayList<ParticipantReportDto>();
    for (final var dto : dtos) {
      dtoList.add(ParticipantReportMapper.map(dto));
    }

    final var reportData = new ParticipantListReportDto(dtoList);

    final var filename = "participant_list_report.pdf";
    final var pdfBytes = reportRenderer.renderReportPdf(reportData);

    return new FileDto(filename, pdfBytes);
  }
}
