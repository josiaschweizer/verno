package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseScheduleClient;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public class CourseScheduleActivateWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.PLANNED;

  @Nonnull private final Lazy<CourseScheduleClient> courseScheduleClient;

  @Inject
  public CourseScheduleActivateWidget(@Nonnull final Injector injector) {
    super("courseSchedule.activate.course.schedules",
            "courseSchedule.activate.course.schedules.that.are.currently.in.planned.status.review.their.details.and.move.them.to.active.when.they.are.ready.to.start",
            "courseSchedule.activate.course.schedules"
    );
    this.courseScheduleClient = Lazy.of(() -> injector.getInstance(CourseScheduleClient.class));

    addActionButtonClickListener(event -> actionButtonClicked(injector));
    setWidthFull();
  }

  private void actionButtonClicked(@Nonnull final Injector injector) {
    if (courseScheduleClient.get().getCourseScheduleByStatus(COURSE_SCHEDULE_STATUS).isEmpty()) {
      final var notificationMessage = getTranslation("courseSchedule.no.planned.course.schedules.available.there.are.currently.no.course.schedules.that.can.be.activated");
      NotificationFactory.showInfoNotification(notificationMessage);
    } else {
      final var dialog = new CourseScheduleDialog(injector, courseScheduleClient.get(), COURSE_SCHEDULE_STATUS, false);
      dialog.open();
    }
  }
}
