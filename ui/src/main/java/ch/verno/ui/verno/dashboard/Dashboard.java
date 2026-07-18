package ch.verno.ui.verno.dashboard;

import ch.verno.common.type.course.courseschedule.status.CourseScheduleStatus;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.course.CourseWidgetGroup;
import ch.verno.ui.verno.dashboard.courseSchedules.CourseScheduleLifecycleWidgetGroup;
import ch.verno.ui.verno.dashboard.io.ImportExportWidgetGroup;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import jakarta.annotation.Nonnull;

public class Dashboard extends VerticalLayout {

  @Nonnull private final Injector injector;

  public Dashboard(@Nonnull final Injector injector) {
    this.injector = injector;

    setSizeFull();
    setPadding(false);
    setSpacing(false);
    add(createCourseTabView());
  }

  @Nonnull
  private TabSheet createCourseTabView() {
    final var tabSheet = new TabSheet();
    tabSheet.setWidthFull();

    final var plannedTab = new CourseWidgetGroup(injector, CourseScheduleStatus.PLANNED);
    final var activeTab = new CourseWidgetGroup(injector, CourseScheduleStatus.ACTIVE);
    final var lifecycleTab = new CourseScheduleLifecycleWidgetGroup(injector);
    final var ioTab = injector.getInstance(ImportExportWidgetGroup.class);

    tabSheet.add(getTranslation(CourseScheduleStatus.PLANNED.getDisplayNameKey()), plannedTab);
    tabSheet.add(getTranslation(CourseScheduleStatus.ACTIVE.getDisplayNameKey()), activeTab);
    tabSheet.add(getTranslation("courseSchedule.course.schedules.lifecycle"), lifecycleTab);
    tabSheet.add(getTranslation("shared.import.export"), ioTab);

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
