package ch.verno.common.report;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IReportServerGate {

  @Nonnull
  ReportDto generateCourseReportPdf(@Nonnull CourseDto course);

  @Nonnull
  ReportDto generateCourseReportPdf(@Nonnull CourseDto course,
                                    @Nonnull List<ParticipantDto> participants);

}
