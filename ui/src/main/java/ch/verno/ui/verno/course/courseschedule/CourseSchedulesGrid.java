package ch.verno.ui.verno.course.courseschedule;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.filter.CourseScheduleFilter;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSE_SCHEDULES)
@Menu(order = 3.2, icon = "vaadin:calendar", title = "courseSchedule.course.schedules")
public class CourseSchedulesGrid extends BaseOverviewGrid<CourseScheduleDto, CourseScheduleFilter> implements HasDynamicTitle {

  @Nonnull
  private final CourseScheduleService courseScheduleService;

  public CourseSchedulesGrid(@Nonnull final CourseScheduleService courseScheduleService) {
    super(CourseScheduleFilter.empty());
    this.courseScheduleService = courseScheduleService;
  }


  @Nonnull
  @Override
  protected Stream<CourseScheduleDto> fetch(@Nonnull final Query<CourseScheduleDto, CourseScheduleFilter> query,
                                            @Nonnull final CourseScheduleFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return courseScheduleService.findCourseSchedules(filter, offset, limit, sortOrders).stream();
  }

  @Override
  protected int count(@Nonnull final Query<CourseScheduleDto, CourseScheduleFilter> query,
                      @Nonnull final CourseScheduleFilter filter) {
    return courseScheduleService.countCourses(filter);
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("courseSchedule.course.schedule");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSE_SCHEDULES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<CourseScheduleDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<CourseScheduleDto, Object>, String>();
    columnsMap.put(CourseScheduleDto::getTitle, getTranslation("shared.title"));
    columnsMap.put(CourseScheduleDto::getWeeksAsString, getTranslation("courseSchedule.weeks"));
    return columnsMap;
  }

  @Nonnull
  @Override
  protected CourseScheduleFilter withSearchText(@Nonnull final String searchText) {
    return CourseScheduleFilter.fromSearchText(searchText);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("courseSchedule.course.schedules");
  }
}
