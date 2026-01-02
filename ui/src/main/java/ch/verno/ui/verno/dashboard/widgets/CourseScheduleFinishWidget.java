package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleFinishWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.ACTIVE;

  public CourseScheduleFinishWidget(@Nonnull final CourseScheduleService courseScheduleService) {
    super("courseSchedule.finish.course.schedules",
            "courseSchedule.finish.course.schedules.that.have.reached.their.end.completed.course.schedules.will.no.longer.appear.in.the.list.of.active.course.schedules",
            "courseSchedule.finish.course.schedules"
    );
    super.addActionButtonClickListener(event -> actionButtonClicked(courseScheduleService));

    setWidthFull();
  }

  private void actionButtonClicked(@Nonnull final CourseScheduleService courseScheduleService) {
    final var courseSchedules = courseScheduleService.getCourseSchedulesByStatus(COURSE_SCHEDULE_STATUS);

    if (courseSchedules.isEmpty()) {
      NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.active.course.schedules.available.to.finish"));
    } else {
      final var dialog = new CourseScheduleDialog(courseScheduleService, COURSE_SCHEDULE_STATUS);
      dialog.open();
    }
  }
}
