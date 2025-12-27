package ch.verno.ui.verno.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PermitAll
@Route(Routes.COURSES)
@PageTitle("Courses")
@Menu(order = 3, icon = "vaadin:desktop", title = "Courses")
public class CoursesGrid extends BaseOverviewGrid<CourseDto> {

  @Nonnull
  private final CourseService courseService;

  public CoursesGrid(@Nonnull final CourseService courseService) {
    this.courseService = courseService;
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
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseDto, Object>, String>();
    columnsMap.put(CourseDto::getTitle, "Title");
    columnsMap.put(CourseDto::getCapacity, "max Capacity");
    columnsMap.put(CourseDto::getWeekdaysAsString, "Weekdays");
    columnsMap.put(CourseDto::getInstructorAsString, "Instructor");
    columnsMap.put(CourseDto::getCourseScheduleAsString, "Schedule");
    columnsMap.put(CourseDto::getCourseLevelAsString, "Level");
    columnsMap.put(CourseDto::getDuration, "Duration [min]");
    return columnsMap;
  }
}
