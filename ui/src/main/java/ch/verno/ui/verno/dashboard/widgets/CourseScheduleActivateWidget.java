package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleActivateWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.PLANNED;

  public CourseScheduleActivateWidget(@Nonnull final CourseScheduleService courseScheduleService) {
    super("courseSchedule.activate.course.schedules",
            "courseSchedule.activate.course.schedules.that.are.currently.in.planned.status.review.their.details.and.move.them.to.active.when.they.are.ready.to.start",
            "courseSchedule.activate.course.schedules"
    );
    addActionButtonClickListener(event -> actionButtonClicked(courseScheduleService));
    setWidthFull();
  }

  private void actionButtonClicked(@Nonnull final CourseScheduleService courseScheduleService) {
    final var courseSchedules = courseScheduleService.getCourseSchedulesByStatus(COURSE_SCHEDULE_STATUS);
    if (courseSchedules.isEmpty()) {
      final var notificationMessage = getTranslation("courseSchedule.no.planned.course.schedules.available.there.are.currently.no.course.schedules.that.can.be.activated");
      NotificationFactory.showInfoNotification(notificationMessage);
    } else {
      final var dialog = new CourseScheduleDialog(courseScheduleService, COURSE_SCHEDULE_STATUS);
      dialog.open();
    }
  }
}
