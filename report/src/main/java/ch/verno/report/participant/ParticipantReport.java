package ch.verno.report.participant;

import ch.verno.common.exceptions.report.PDFRendererException;
import ch.verno.report.base.BaseReport;
import ch.verno.report.dto.ParticipantListReportDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

public class ParticipantReport extends BaseReport<ParticipantListReportDto> {

  public ParticipantReport(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] generateReportPdf(@Nonnull final ParticipantListReportDto dto) {
    final var context = new Context();
    context.setVariable("participants", dto.participantList());

    final var html = templateEngine.process(getTemplate(), context);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      final var pdfBuilder = new PdfRendererBuilder();
      pdfBuilder.useFastMode();
      pdfBuilder.withHtmlContent(html, null);
      pdfBuilder.toStream(out);
      pdfBuilder.run();
      return out.toByteArray();
    } catch (Exception e) {
      throw new PDFRendererException("Failed to render participant list PDF", e);
    }
  }

  @Nonnull
  @Override
  protected String getTemplate() {
    return "reports/participants-report";
  }

}
