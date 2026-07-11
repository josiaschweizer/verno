package ch.verno.ui.verno.dashboard.course;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.ui.verno.course.courses.detail.CourseDetail;
import com.google.inject.Injector;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class CourseDetailDialog extends Dialog {

  @Nonnull private final Injector injector;
  @Nonnull private final CourseDto currentCourse;

  public CourseDetailDialog(@Nonnull final Injector injector,
                            @Nonnull final CourseDto currentCourse) {
    this.injector = injector;
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
    final var courseDetail = new CourseDetail(injector, false, false);
    courseDetail.setParameter(null, currentCourse.getId());
    courseDetail.setAfterSave(this::close);
    courseDetail.setPadding(false);
    courseDetail.setMargin(false);
    return courseDetail;
  }

}
