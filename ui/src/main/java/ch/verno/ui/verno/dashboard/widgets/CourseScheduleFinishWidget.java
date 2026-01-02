package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleFinishWidget extends VASimpleBaseDashboardWidget {

  public CourseScheduleFinishWidget(@Nonnull final CourseScheduleService courseScheduleService) {
    super("Finish Course Schedules",
            "Finish course schedules that have reached their end date and are not longer interesting. After you have finished a course schedule, it will no longer appear in the list of active course schedules.",
            "Finish Course Schedules",
            event -> {
              final var dialog = new CourseScheduleDialog(courseScheduleService);
              dialog.open();
            }
    );

    setWidthFull();
  }
}
