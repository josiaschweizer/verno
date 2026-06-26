package ch.verno.server.report.participant;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.report.dto.ParticipantListReportDto;
import ch.verno.report.dto.ParticipantReportDto;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.report.base.usecase.BaseListReportUseCase;
import ch.verno.server.service.intern.table.participant.ParticipantService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParticipantReportUseCase implements BaseListReportUseCase<ParticipantDto> {

  @Nonnull private final Lazy<ParticipantService> participantService;
  @Nonnull private final Lazy<ParticipantReportRenderer> reportRenderer;

  public ParticipantReportUseCase(@Nonnull final ServerBean serverBean) {
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
    this.reportRenderer = Lazy.of(() -> serverBean.get(ParticipantReportRenderer.class));
  }

  @Nonnull
  @Override
  public FileDto generate() {
    final var participants = participantService.get().findAll();
    return generate(participants);
  }

  @Nonnull
  @Override
  public FileDto generate(@Nonnull final List<ParticipantDto> dtos) {
    final var dtoList = new ArrayList<ParticipantReportDto>();
    for (final var dto : dtos) {
      dtoList.add(ParticipantReportMapper.map(dto));
    }

    final var reportData = new ParticipantListReportDto(dtoList);

    final var filename = "participant_list_report.pdf";
    final var pdfBytes = reportRenderer.get().renderReportPdf(reportData, null);

    return new FileDto(filename, pdfBytes);
  }
}
