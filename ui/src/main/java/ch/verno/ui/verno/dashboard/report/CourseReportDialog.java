package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.lib.api.ApiUrl;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.ui.base.report.BaseReportPreviewDialog;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseReportDialog extends BaseReportPreviewDialog {

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final List<ParticipantDto> participantsInCourse;

  public CourseReportDialog(@Nonnull final Injector injector,
                            @Nonnull final CourseDto currentCourse,
                            @Nonnull final List<ParticipantDto> participantsInCourse) {
    super(injector);
    this.currentCourse = currentCourse;
    this.participantsInCourse = participantsInCourse;

    init();
  }

  @Nonnull
  @Override
  protected String generateReport() {
    return reportClient.get().generateCourseReport(currentCourse, participantsInCourse);
  }

  @Nonnull
  @Override
  protected String getApiUrl() {
    return ApiUrl.TEMP_FILE_REPORT_PUBLIC;
  }

}