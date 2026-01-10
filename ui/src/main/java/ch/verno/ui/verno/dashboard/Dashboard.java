package ch.verno.ui.verno.dashboard;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.report.IReportServerGate;
import ch.verno.server.service.*;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.course.CourseWidgetGroup;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleLifecycleWidgetGroup;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import jakarta.annotation.Nonnull;

public class Dashboard extends VerticalLayout {

  @Nonnull private final ParticipantService participantService;
  @Nonnull private final CourseService courseService;
  @Nonnull private final CourseLevelService courseLevelService;
  @Nonnull private final CourseScheduleService courseScheduleService;
  @Nonnull private final MandantSettingService mandantSettingService;
  @Nonnull private final IReportServerGate reportServerGate;

  public Dashboard(@Nonnull final CourseService courseService,
                   @Nonnull final ParticipantService participantService,
                   @Nonnull final CourseLevelService courseLevelService,
                   @Nonnull final CourseScheduleService courseScheduleService,
                   @Nonnull final MandantSettingService mandantSettingService,
                   @Nonnull final IReportServerGate reportServerGate) {
    this.participantService = participantService;
    this.courseService = courseService;
    this.courseLevelService = courseLevelService;
    this.courseScheduleService = courseScheduleService;
    this.mandantSettingService = mandantSettingService;
    this.reportServerGate = reportServerGate;

    setSizeFull();
    setPadding(false);
    setSpacing(false);
    add(createCourseTabView());
  }

  @Nonnull
  private TabSheet createCourseTabView() {
    final var tabSheet = new TabSheet();
    tabSheet.setWidthFull();

    final var plannedTab = new CourseWidgetGroup(CourseScheduleStatus.PLANNED, courseService, participantService, courseLevelService, mandantSettingService,  reportServerGate);
    final var activeTab = new CourseWidgetGroup(CourseScheduleStatus.ACTIVE, courseService, participantService, courseLevelService, mandantSettingService, reportServerGate);
    final var lifecycleTab = new CourseScheduleLifecycleWidgetGroup(courseScheduleService);

    tabSheet.add(getTranslation(CourseScheduleStatus.PLANNED.getDisplayNameKey()), plannedTab);
    tabSheet.add(getTranslation(CourseScheduleStatus.ACTIVE.getDisplayNameKey()), activeTab);
    tabSheet.add(getTranslation("courseSchedule.course.schedules.lifecycle"), lifecycleTab);

    tabSheet.addSelectedChangeListener(event -> {
      final var selectedTab = event.getSelectedTab();

      if (selectedTab != null) {
        final var content = tabSheet.getComponent(selectedTab);

        if (content instanceof Refreshable refreshable) {
          refreshable.refresh();
        }
      }
    });

    return tabSheet;
  }

}
