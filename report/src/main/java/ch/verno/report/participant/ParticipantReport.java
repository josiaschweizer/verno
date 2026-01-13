package ch.verno.report.participant;

import ch.verno.report.base.BaseReport;
import ch.verno.report.dto.ParticipantListReportDto;
import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;

public class ParticipantReport extends BaseReport<ParticipantListReportDto> {

  public ParticipantReport(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] generateReportPdf(@Nonnull final ParticipantListReportDto reportData) {
    return new byte[0];
  }

  @Nonnull
  @Override
  protected String getTemplate() {
    return "reports/participants-report";
  }

}
