package ch.verno.report.course;

import ch.verno.common.exceptions.report.PDFRendererException;
import ch.verno.report.base.BaseReport;
import ch.verno.report.dto.CourseReportDto;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class CourseReport extends BaseReport<CourseReportDto> {

  @Nullable private final byte[] templateBytes;

  public CourseReport(@Nonnull final TemplateEngine templateEngine,
                      @Nullable final byte[] templateBytes) {
    super(templateEngine);

    this.templateBytes = templateBytes;
  }

  @Override
  public byte[] generateReportPdf(@Nonnull final CourseReportDto course) {
    final var context = new Context();
    context.setVariable("course", course);

    String processedHtml;
    if (templateBytes != null) {
      final var templateHtml = new String(templateBytes, StandardCharsets.UTF_8);
      processedHtml = createCustomTemplateEngine().process(templateHtml, context);
    } else {
      processedHtml = templateEngine.process(getTemplate(), context);
    }



    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      final var pdfBuilder = new PdfRendererBuilder();
      pdfBuilder.useFastMode();
      pdfBuilder.withHtmlContent(processedHtml, null);
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