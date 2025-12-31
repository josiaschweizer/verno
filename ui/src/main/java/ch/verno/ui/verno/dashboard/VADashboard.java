package ch.verno.ui.verno.dashboard;

import ch.verno.server.service.CourseService;
import ch.verno.server.service.MandantSettingService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.dashboard.VABaseDashboard;
import ch.verno.ui.base.components.dashboard.VABaseDashboardWidget;
import ch.verno.ui.verno.dashboard.widgets.AssignParticipantDashboardWidget;
import jakarta.annotation.Nonnull;

import java.util.List;

public class VADashboard extends VABaseDashboard {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final MandantSettingService mandantSettingService;

  public VADashboard(@Nonnull final CourseService courseService,
                     @Nonnull final ParticipantService participantService,
                     @Nonnull final MandantSettingService mandantSettingService) {
    super();
    this.participantService = participantService;
    this.courseService = courseService;
    this.mandantSettingService = mandantSettingService;

    getDashboardWidgets().forEach(this::addWidget);
  }

  @Nonnull
  private List<VABaseDashboardWidget> getDashboardWidgets() {
    return List.of(
            new AssignParticipantDashboardWidget(courseService, participantService, mandantSettingService)
    );
  }

}
