package ch.verno.ui.verno.course.courses;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSES)
@PageTitle("Courses")
@Menu(order = 3.1, icon = "vaadin:desktop", title = "Courses")
public class CoursesGrid extends BaseOverviewGrid<CourseDto, CourseFilter> {

  @Nonnull
  private final CourseService courseService;

  public CoursesGrid(@Nonnull final CourseService courseService) {
    super(CourseFilter.empty());
    this.courseService = courseService;
  }

  @Nonnull
  @Override
  protected Stream<CourseDto> fetch(@Nonnull final Query<CourseDto, CourseFilter> query, @Nonnull final CourseFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return courseService.findCourses(filter, offset, limit, sortOrders).stream();
  }

  @Override
  protected int count(@Nonnull final Query<CourseDto, CourseFilter> query,
                      @Nonnull final CourseFilter filter) {
    return courseService.countCourses(filter);
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.COURSE;
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseDto, Object>, String>();
    columnsMap.put(CourseDto::getTitle, getTranslation("shared.title"));
    columnsMap.put(CourseDto::getCapacity, getTranslation("course.max.capacity"));
    columnsMap.put(CourseDto::getWeekdaysAsString, getTranslation("course.weekdays"));
    columnsMap.put(CourseDto::getInstructorAsString, getTranslation("shared.instructor"));
    columnsMap.put(CourseDto::getCourseScheduleAsString, getTranslation("course.schedule"));
    columnsMap.put(CourseDto::getCourseLevelAsString, getTranslation("course.level"));
    columnsMap.put(CourseDto::getStartTime, getTranslation("course.start.time"));
    columnsMap.put(CourseDto::getEndTime, getTranslation("course.end.time"));
    return columnsMap;
  }

  @Nonnull
  @Override
  protected CourseFilter withSearchText(@Nonnull final String searchText) {
    return CourseFilter.fromSearchText(searchText);
  }
}
