package ch.verno.report.course;

import ch.verno.common.exceptions.report.PDFRendererException;
import ch.verno.report.base.BaseReport;
import ch.verno.report.dto.CourseReportDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

public class CourseReport extends BaseReport<CourseReportDto> {

  public CourseReport(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] generateReportPdf(@Nonnull final CourseReportDto course) {
    final var context = new Context();
    context.setVariable("course", course);

    final var html = templateEngine.process(getTemplate(), context);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      final var pdfBuilder = new PdfRendererBuilder();
      pdfBuilder.useFastMode();
      pdfBuilder.withHtmlContent(html, null);
      pdfBuilder.toStream(out);
      pdfBuilder.run();
      return out.toByteArray();
    } catch (Exception e) {
      throw new PDFRendererException("Failed to render course report PDF", e);
    }
  }

  @Nonnull
  @Override
  protected String getTemplate() {
    return "reports/course-report";
  }
}