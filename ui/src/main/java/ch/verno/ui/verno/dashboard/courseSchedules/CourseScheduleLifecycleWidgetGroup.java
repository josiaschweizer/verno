package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.common.db.service.ICourseScheduleService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleActivateWidget;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleFinishWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleLifecycleWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull
  private final ICourseScheduleService courseScheduleService;

  public CourseScheduleLifecycleWidgetGroup(@Nonnull final GlobalInterface globalInterface) {
    this.courseScheduleService = globalInterface.getService(ICourseScheduleService.class);

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
