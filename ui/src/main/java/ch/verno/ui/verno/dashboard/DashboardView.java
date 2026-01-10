package ch.verno.ui.verno.dashboard;

import ch.verno.server.service.*;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.common.report.IReportServerGate;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

  public DashboardView(@Nonnull final CourseService courseService,
                       @Nonnull final ParticipantService participantService,
                       @Nonnull final CourseLevelService courseLevelService,
                       @Nonnull final CourseScheduleService courseScheduleService,
                       @Nonnull final MandantSettingService mandantSettingService,
                       @Nonnull final IReportServerGate reportServerGate) {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    setAlignItems(Alignment.STRETCH);

    final var dashboard = new Dashboard(courseService, participantService, courseLevelService, courseScheduleService, mandantSettingService, reportServerGate);

    add(createViewToolBar());
    add(dashboard);
  }

  @Nonnull
  private ViewToolbar createViewToolBar() {
    return ViewToolbarFactory.createSimpleToolbar(getTranslation("shared.dashboard"));
  }

}
