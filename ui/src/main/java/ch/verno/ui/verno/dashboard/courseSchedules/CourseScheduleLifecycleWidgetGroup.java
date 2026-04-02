package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.common.db.service.intern.ICourseScheduleService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleActivateWidget;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleFinishWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleLifecycleWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final GlobalInterface globalInterface;

  public CourseScheduleLifecycleWidgetGroup(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;

    setWidthFull();

    init();
  }

  private void init() {
    add(new CourseScheduleActivateWidget(globalInterface));
    add(new CourseScheduleFinishWidget(globalInterface));
  }

  @Override
  public void refresh() {
    removeAll();
    init();
  }

}
