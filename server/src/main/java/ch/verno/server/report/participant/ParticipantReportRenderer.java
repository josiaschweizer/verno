package ch.verno.server.report.participant;

import ch.verno.report.dto.ParticipantListReportDto;
import ch.verno.report.participant.ParticipantReport;
import ch.verno.server.report.base.renderer.BaseReportRenderer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class ParticipantReportRenderer extends BaseReportRenderer<ParticipantListReportDto> {

  public ParticipantReportRenderer(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] renderReportPdf(@Nonnull final ParticipantListReportDto reportData,
                                @Nullable final byte[] reportTemplate) {
    return new ParticipantReport(templateEngine).generateReportPdf(reportData);
  }
}
