package ch.verno.server.report.course;

import ch.verno.report.course.CourseReport;
import ch.verno.report.dto.CourseReportDto;
import ch.verno.server.report.base.renderer.BaseReportRenderer;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class CourseReportRenderer extends BaseReportRenderer<CourseReportDto> {

  public CourseReportRenderer(@Nonnull final TemplateEngine templateEngine) {
    super(templateEngine);
  }

  @Override
  public byte[] renderReportPdf(@Nonnull final CourseReportDto reportData) {
    return new CourseReport(templateEngine).generateReportPdf(reportData);
  }

}
