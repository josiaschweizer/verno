package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleActivateWidget;
import ch.verno.ui.verno.dashboard.widgets.CourseScheduleFinishWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleLifecycleWidgetGroup extends VerticalLayout {

  public CourseScheduleLifecycleWidgetGroup(@Nonnull final CourseScheduleService courseScheduleService) {
    add(new CourseScheduleFinishWidget(courseScheduleService));
    add(new CourseScheduleActivateWidget(courseScheduleService));

    setWidthFull();
    setHeightFull();
  }

}
