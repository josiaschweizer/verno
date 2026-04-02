package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.db.service.intern.ICourseScheduleService;
import ch.verno.common.db.type.CourseScheduleStatus;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleFinishWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.ACTIVE;

  public CourseScheduleFinishWidget(@Nonnull final GlobalInterface globalInterface) {
    super("courseSchedule.finish.course.schedules",
            "courseSchedule.finish.course.schedules.that.have.reached.their.end.completed.course.schedules.will.no.longer.appear.in.the.list.of.active.course.schedules",
            "courseSchedule.finish.course.schedules"
    );
    super.addActionButtonClickListener(event -> actionButtonClicked(globalInterface));

    setWidthFull();
  }

  private void actionButtonClicked(@Nonnull final GlobalInterface globalInterface) {
    final var courseScheduleService = globalInterface.getService(ICourseScheduleService.class);

    if (courseScheduleService.getCourseSchedulesByStatus(COURSE_SCHEDULE_STATUS).isEmpty()) {
      NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.active.course.schedules.available.to.finish"));
    } else {
      final var dialog = new CourseScheduleDialog(globalInterface, COURSE_SCHEDULE_STATUS, true);
      dialog.open();
    }
  }
}
