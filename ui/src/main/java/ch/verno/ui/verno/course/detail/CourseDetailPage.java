package ch.verno.ui.verno.course.detail;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.detail.BaseDetailPage;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

@Route(Routes.COURSES + Routes.DETAIL)
@PageTitle("Courses")
@Menu(order = 3.1, icon = "vaadin:mobile", title = "Course Detail")
public class CourseDetailPage extends BaseDetailPage<CourseDto> {

  @Nonnull
  private final CourseService courseService;

  public CourseDetailPage(@Nonnull final CourseService courseService) {
    this.courseService = courseService;
  }

  @Override
  protected void initUI() {

  }

  @NonNull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.COURSE;
  }

  @NonNull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSES;
  }

  @NonNull
  @Override
  protected Binder<CourseDto> createBinder() {
    return new Binder<>(CourseDto.class);
  }

  @NonNull
  @Override
  protected CourseDto createBean(@NonNull final CourseDto bean) {
    return courseService.createCourse(bean);
  }

  @NonNull
  @Override
  protected CourseDto updateBean(@NonNull final CourseDto bean) {
    return courseService.updateCourse(bean);
  }

  @NonNull
  @Override
  protected CourseDto newBeanInstance() {
    return new CourseDto();
  }

  @Override
  protected CourseDto getBeanById(@NonNull final Long id) {
    return courseService.getCourseById(id);  }
}
