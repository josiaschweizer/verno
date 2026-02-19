package ch.verno.server.report.course;

import ch.verno.report.course.CourseReport;
import ch.verno.report.dto.CourseReportDto;
import ch.verno.server.report.base.renderer.BaseReportRenderer;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class CourseReportRenderer extends BaseReportRenderer<CourseReportDto> {

  public CourseReportRenderer(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] renderReportPdf(@Nonnull final CourseReportDto reportData,
                                @Nullable final byte[] reportTemplate) {
    return new CourseReport(templateEngine, reportTemplate).generateReportPdf(reportData);
  }

}
