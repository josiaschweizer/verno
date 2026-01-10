package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.report.IReportServerGate;
import ch.verno.ui.base.Refreshable;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final CourseScheduleStatus status;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final IParticipantService participantService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final IMandantSettingService mandantSettingService;
  @Nonnull private final IReportServerGate reportServerGate;

  public CourseWidgetGroup(@Nonnull final CourseScheduleStatus status,
                           @Nonnull final ICourseService courseService,
                           @Nonnull final IParticipantService participantService,
                           @Nonnull final ICourseLevelService courseLevelService,
                           @Nonnull final IMandantSettingService mandantSettingService,
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
