package ch.verno.ui.verno.course.courseschedule;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.filter.CourseScheduleFilter;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.base.grid.ComponentGridColumn;
import ch.verno.ui.base.grid.ObjectGridColumn;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
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
    return courseScheduleService.countCourseSchedules(filter);
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
  protected List<ObjectGridColumn<CourseScheduleDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<CourseScheduleDto>>();
    columns.add(new ObjectGridColumn<>("title", CourseScheduleDto::getTitle, getTranslation("shared.title"), true));
    columns.add(new ObjectGridColumn<>("first-week", dto -> dto.getWeeks().getFirst(), getTranslation("courseSchedule.first.week"), false));
    columns.add(new ObjectGridColumn<>("last-week", dto -> dto.getWeeks().getLast(), getTranslation("courseSchedule.last.week"), false));
    columns.add(new ObjectGridColumn<>("weeks", CourseScheduleDto::getWeeksAsString, getTranslation("courseSchedule.weeks"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<CourseScheduleDto>> getComponentColumns() {
    final var componentColumns = new ArrayList<ComponentGridColumn<CourseScheduleDto>>();
    componentColumns.add(new ComponentGridColumn<>("status", this::getStatusBadge, getTranslation("shared.status"), true));
    return componentColumns;
  }

  @Nonnull
  private Span getStatusBadge(@Nonnull final CourseScheduleDto dto) {
    final var status = dto.getStatus();
    final var statusSpan = new Span(getTranslation(status.getDisplayNameKey()));
    statusSpan.getElement().getThemeList().add(status.getBadgeLabelClassName());
    return statusSpan;
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
