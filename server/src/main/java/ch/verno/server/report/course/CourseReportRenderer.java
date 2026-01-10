package ch.verno.server.report.course;

import ch.verno.report.course.CourseReport;
import ch.verno.report.dto.CourseReportDto;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
public class CourseReportRenderer {

  @Nonnull private final TemplateEngine templateEngine;

  public CourseReportRenderer(@Nonnull final TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public byte[] renderReportPdf(@Nonnull final CourseReportDto reportData) {
    return new CourseReport(templateEngine).generateReportPdf(reportData);
  }

}
