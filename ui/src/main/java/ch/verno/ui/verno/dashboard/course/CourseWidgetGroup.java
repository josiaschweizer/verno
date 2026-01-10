package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.report.IReportServerGate;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.Refreshable;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final CourseScheduleStatus status;
  @Nonnull private final CourseService courseService;
  @Nonnull private final ParticipantService participantService;
  @Nonnull private final CourseLevelService courseLevelService;
  @Nonnull private final MandantSettingService mandantSettingService;
  @Nonnull private final IReportServerGate reportServerGate;

  public CourseWidgetGroup(@Nonnull final CourseScheduleStatus status,
                           @Nonnull final CourseService courseService,
                           @Nonnull final ParticipantService participantService,
                           @Nonnull final CourseLevelService courseLevelService,
                           @Nonnull final MandantSettingService mandantSettingService,
                           @Nonnull final IReportServerGate reportServerGate) {
    this.status = status;
    this.courseService = courseService;
    this.participantService = participantService;
    this.courseLevelService = courseLevelService;
    this.mandantSettingService = mandantSettingService;
    this.reportServerGate = reportServerGate;

    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    init();
  }

  private void init() {
    final var courses = courseService.getCoursesByCourseScheduleStatus(status);

    for (final var course : courses) {
      if (course.getId() != null) {
        add(new CourseWidget(course.getId(), courseService, participantService, courseLevelService, mandantSettingService, reportServerGate));
      }
    }
  }

  @Override
  public void refresh() {
    removeAll();
    init();
  }
}
