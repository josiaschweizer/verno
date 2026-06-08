package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.server.service.intern.ICourseService;
import ch.verno.common.db.type.CourseScheduleStatus;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.Refreshable;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final CourseScheduleStatus status;

  public CourseWidgetGroup(@Nonnull final GlobalInterface globalInterface,
                           @Nonnull final CourseScheduleStatus status) {
    this.globalInterface = globalInterface;
    this.courseService = globalInterface.getService(ICourseService.class);
    this.status = status;

    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    init();
  }

  private void init() {
    final var courses = courseService.getCoursesByCourseScheduleStatus(status);

    for (final var course : courses) {
      if (course.getId() != null) {
        add(new CourseWidget(course.getId(), globalInterface));
      }
    }
  }

  @Override
  public void refresh() {
    removeAll();
    init();
  }
}
