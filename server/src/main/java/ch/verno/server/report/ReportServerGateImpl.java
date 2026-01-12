package ch.verno.server.report;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.report.ReportDto;
import ch.verno.common.report.ReportServerGate;
import ch.verno.server.report.course.CourseReportUseCase;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class ReportServerGateImpl implements ReportServerGate {

  @Nonnull private final CourseReportUseCase courseReportUseCase;

  public ReportServerGateImpl(@Nonnull final CourseReportUseCase courseReportUseCase) {
    this.courseReportUseCase = courseReportUseCase;
  }

  @Nonnull
  @Override
  public ReportDto generateCourseReportPdf(@Nonnull final CourseDto course) {
    return courseReportUseCase.generate(course);
  }

  @Nonnull
  @Override
  public ReportDto generateCourseReportPdf(@Nonnull final CourseDto course,
                                           @Nonnull final java.util.List<ParticipantDto> participants) {
    return courseReportUseCase.generate(course, participants);
  }
}
