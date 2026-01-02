package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleDialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleActivateWidget extends VASimpleBaseDashboardWidget {

  public CourseScheduleActivateWidget(@Nonnull final CourseScheduleService courseScheduleService) {
    super("Activate Course Schedules",
            "Active course schedules that are currently ongoing and still are in planned status. You can view details about these course schedules and manage them as needed.",
            "Activate Course Schedules",
            event -> {
              final var dialog = new CourseScheduleDialog(courseScheduleService);
              dialog.open();
            }
    );

    setWidthFull();
  }
}
