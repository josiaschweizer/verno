package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.db.type.CourseScheduleStatus;
import ch.verno.common.db.service.intern.ICourseScheduleService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleActivateWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.PLANNED;

  public CourseScheduleActivateWidget(@Nonnull final GlobalInterface globalInterface) {
    super("courseSchedule.activate.course.schedules",
            "courseSchedule.activate.course.schedules.that.are.currently.in.planned.status.review.their.details.and.move.them.to.active.when.they.are.ready.to.start",
            "courseSchedule.activate.course.schedules"
    );
    addActionButtonClickListener(event -> actionButtonClicked(globalInterface));
    setWidthFull();
  }

  private void actionButtonClicked(@Nonnull final GlobalInterface globalInterface) {
    final var courseScheduleService = globalInterface.getService(ICourseScheduleService.class);

    if (courseScheduleService.getCourseSchedulesByStatus(COURSE_SCHEDULE_STATUS).isEmpty()) {
      final var notificationMessage = getTranslation("courseSchedule.no.planned.course.schedules.available.there.are.currently.no.course.schedules.that.can.be.activated");
      NotificationFactory.showInfoNotification(notificationMessage);
    } else {
      final var dialog = new CourseScheduleDialog(globalInterface, COURSE_SCHEDULE_STATUS, false);
      dialog.open();
    }
  }
}
