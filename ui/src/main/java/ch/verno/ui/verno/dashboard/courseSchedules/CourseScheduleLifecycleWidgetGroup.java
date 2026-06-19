package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.Refreshable;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleActivateWidget;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleFinishWidget;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleLifecycleWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final Injector injector;

  public CourseScheduleLifecycleWidgetGroup(@Nonnull final Injector injector) {
    this.injector = injector;

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
