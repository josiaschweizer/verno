package ch.verno.ui.verno.dashboard.widgets;

import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.dashboard.VASimpleBaseDashboardWidget;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import jakarta.annotation.Nonnull;

public class AssignParticipantDashboardWidget extends VASimpleBaseDashboardWidget {

  public AssignParticipantDashboardWidget(@Nonnull CourseService courseService,
                                          @Nonnull ParticipantService participantService,
                                          @Nonnull final MandantSettingService mandantSettingService) {
    super("Assign Participants",
            "Assign participants to available courses to get them started.",
            "Assign Now",
            event -> {
              final var dialog = new AssignToCourseDialog(courseService, participantService, mandantSettingService);
              dialog.open();
            });
  }
}
