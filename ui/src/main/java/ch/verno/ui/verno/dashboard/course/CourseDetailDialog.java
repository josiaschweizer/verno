package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.gate.GlobalGate;
import ch.verno.ui.verno.course.courses.detail.CourseDetail;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseDetailDialog extends Dialog {

  @Nonnull private final GlobalGate globalGate;
  @Nonnull private final CourseDto currentCourse;

  public CourseDetailDialog(@Nonnull final GlobalGate globalGate,
                            @Nonnull final CourseDto currentCourse) {
    this.globalGate = globalGate;
    this.currentCourse = currentCourse;

    initUI();
  }

  private void initUI() {
    final var content = createCourseDetail();

    setHeight("90vh");
    setWidth("min(1500px, 95vw)");
    setMaxWidth("1500px");
    setMinWidth("320px");

    setHeaderTitle("Course Detail");
    add(content);
  }

  @Nonnull
  private VerticalLayout createCourseDetail() {
    final var courseDetail = new CourseDetail(globalGate,
            false,
            false);
    courseDetail.setParameter(null, currentCourse.getId());
    courseDetail.setAfterSave(this::close);
    courseDetail.setPadding(false);
    courseDetail.setMargin(false);
    return courseDetail;
  }

}
