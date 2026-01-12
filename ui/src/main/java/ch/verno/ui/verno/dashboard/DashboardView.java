package ch.verno.ui.verno.dashboard;

import ch.verno.common.db.service.*;
import ch.verno.common.report.ReportServerGate;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Route("")
@PermitAll
@PageTitle("Dashboard")
public class DashboardView extends VerticalLayout {

  public DashboardView(@Nonnull final ICourseService courseService,
                       @Nonnull final IInstructorService instructorService,
                       @Nonnull final IParticipantService participantService,
                       @Nonnull final ICourseLevelService courseLevelService,
                       @Nonnull final ICourseScheduleService courseScheduleService,
                       @Nonnull final IMandantSettingService mandantSettingService,
                       @Nonnull final ReportServerGate reportServerGate) {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    setAlignItems(Alignment.STRETCH);

    final var dashboard = new Dashboard(courseService, instructorService, participantService, courseLevelService, courseScheduleService, mandantSettingService, reportServerGate);

    add(createViewToolBar());
    add(dashboard);
  }

  @Nonnull
  private ViewToolbar createViewToolBar() {
    return ViewToolbarFactory.createSimpleToolbar(getTranslation("shared.dashboard"));
  }

}
