package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.common.type.course.courseschedule.status.CourseScheduleStatus;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseScheduleClient;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public class CourseScheduleFinishWidget extends VASimpleBaseDashboardWidget {

  public static final CourseScheduleStatus COURSE_SCHEDULE_STATUS = CourseScheduleStatus.ACTIVE;

  @Nonnull private final Lazy<CourseScheduleClient> courseScheduleClient;
  private final Injector injector;

  @Inject
  public CourseScheduleFinishWidget(@Nonnull final Injector injector) {
    super("courseSchedule.finish.course.schedules",
            "courseSchedule.finish.course.schedules.that.have.reached.their.end.completed.course.schedules.will.no.longer.appear.in.the.list.of.active.course.schedules",
            "courseSchedule.finish.course.schedules"
    );
    super.addActionButtonClickListener(event -> actionButtonClicked());

    this.injector = injector;
    this.courseScheduleClient = Lazy.of(() -> injector.getInstance(CourseScheduleClient.class));

    setWidthFull();
  }

  private void actionButtonClicked() {
    if (courseScheduleClient.get().getCourseScheduleByStatus(COURSE_SCHEDULE_STATUS).isEmpty()) {
      NotificationFactory.showInfoNotification(getTranslation("courseSchedule.no.active.course.schedules.available.to.finish"));
    } else {
      final var dialog = new CourseScheduleDialog(injector, courseScheduleClient.get(), COURSE_SCHEDULE_STATUS, true);
      dialog.open();
    }
  }
}
