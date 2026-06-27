package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.type.CourseScheduleStatus;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseClient;
import ch.verno.ui.base.Refreshable;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseWidgetGroup extends VerticalLayout implements Refreshable {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<CourseClient> courseClient;

  @Nonnull private final CourseScheduleStatus status;

  public CourseWidgetGroup(@Nonnull final Injector injector,
                           @Nonnull final CourseScheduleStatus status) {
    this.injector = injector;
    this.courseClient = Lazy.of(() -> injector.getInstance(CourseClient.class));
    this.status = status;

    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    init();
  }

  private void init() {
    final var courses = courseClient.get().getCoursesByCourseScheduleStatus(status);

    for (final var course : courses) {
      if (course.getId() != null) {
        add(new CourseWidget(injector, course.getId()));
      }
    }
  }

  @Override
  public void refresh() {
    removeAll();
    init();
  }
}
