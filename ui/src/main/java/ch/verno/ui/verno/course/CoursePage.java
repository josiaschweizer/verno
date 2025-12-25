package ch.verno.ui.verno.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

@Route(Routes.COURSES)
@PageTitle("Courses")
@Menu(order = 3, icon = "vaadin:desktop", title = "Courses")
public class CoursePage extends BaseOverviewGrid<CourseDto> {

  @Nonnull
  private final CourseService courseService;

  public CoursePage(@Nonnull final CourseService courseService) {
    this.courseService = courseService;
  }

  @Override
  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<CourseDto> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
  }

  @Nonnull
  @Override
  protected List<CourseDto> fetchItems() {
    return courseService.getAllCourses();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.COURSE;
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseDto, Object>, String> getColumns() {
    return Map.of();
  }
}
