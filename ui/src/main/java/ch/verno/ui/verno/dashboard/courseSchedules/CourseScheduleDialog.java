package ch.verno.ui.verno.dashboard.courseSchedules;

import ch.verno.server.service.CourseScheduleService;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nonnull;

public class CourseScheduleDialog extends Dialog {

  @Nonnull
  private final CourseScheduleService courseScheduleService;

  public CourseScheduleDialog(@Nonnull final CourseScheduleService courseScheduleService) {
    this.courseScheduleService = courseScheduleService;

    initUI();
  }

  private void initUI() {

  }

}
