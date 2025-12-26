package ch.verno.ui.verno.course.courseschedule;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Route(Routes.COURSE_SCHEDULES)
@PageTitle("Course Schedules")
@Menu(order = 3.2, icon = "vaadin:calendar", title = "Course Schedules ")
public class CourseSchedulesGrid extends BaseOverviewGrid<CourseScheduleDto> {

  @Nonnull
  private final CourseScheduleService courseScheduleService;

  public CourseSchedulesGrid(@Nonnull final CourseScheduleService courseScheduleService) {
    this.courseScheduleService = courseScheduleService;
  }

  @Nonnull
  @Override
  protected List<CourseScheduleDto> fetchItems() {
    return courseScheduleService.getAllCourseSchedules();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.COURSESCHEDULE;
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseScheduleDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseScheduleDto, Object>, String>();
    columnsMap.put(CourseScheduleDto::getTitle, "Title");
    columnsMap.put(CourseScheduleDto::getWeeksAsString, "Weeks");
    return columnsMap;
  }
}
