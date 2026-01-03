package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleActivateWidget;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleFinishWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleLifecycleWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull
  private final CourseScheduleService courseScheduleService;

  public CourseScheduleLifecycleWidgetGroup(@Nonnull final CourseScheduleService courseScheduleService) {
    this.courseScheduleService = courseScheduleService;

    setWidthFull();

    init();
  }

  private void init() {
    add(new CourseScheduleActivateWidget(courseScheduleService));
    add(new CourseScheduleFinishWidget(courseScheduleService));
  }

  @Override
  public void refresh() {
    removeAll();
    init();
  }

}
