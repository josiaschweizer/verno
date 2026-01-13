package ch.verno.ui.verno.course.courses;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.db.service.ICourseService;
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
import jakarta.annotation.Nullable;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSES)
@Menu(order = 3.1, icon = "vaadin:desktop", title = "course.course.overview")
public class CoursesGrid extends BaseOverviewGrid<CourseDto, CourseFilter> implements HasDynamicTitle {

  @Nonnull
  private final ICourseService courseService;

  public CoursesGrid(@Nonnull final ICourseService courseService) {
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
    return getTranslation("course.course");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<CourseDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<CourseDto>>();
    columns.add(new ObjectGridColumn<>("title", CourseDto::getTitle, getTranslation("shared.title"), true));
    columns.add(new ObjectGridColumn<>("capacity", CourseDto::getCapacity, getTranslation("course.max.capacity"), true));
    columns.add(new ObjectGridColumn<>("weekdays", CourseDto::getWeekdaysAsString, getTranslation("course.weekdays"), true));
    columns.add(new ObjectGridColumn<>("instructor", CourseDto::getInstructorAsString, getTranslation("shared.instructor"), true));
    columns.add(new ObjectGridColumn<>("courseSchedule", CourseDto::getCourseScheduleAsString, getTranslation("course.schedule"), true));
    columns.add(new ObjectGridColumn<>("courseLevels", CourseDto::getCourseLevelAsString, getTranslation("course.level"), true));
    columns.add(new ObjectGridColumn<>("startTime", CourseDto::getStartTime, getTranslation("course.start.time"), true));
    columns.add(new ObjectGridColumn<>("endTime", CourseDto::getEndTime, getTranslation("course.end.time"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<CourseDto>> getComponentColumns() {
    final var componentColumns = new ArrayList<ComponentGridColumn<CourseDto>>();
    componentColumns.add(new ComponentGridColumn<>("courseSchedule.status", this::getStatusBadge, getTranslation("shared.status"), true));
    return componentColumns;
  }

  @Nullable
  private Span getStatusBadge(@Nonnull final CourseDto dto) {
    if (dto.getCourseSchedule() == null) {
      return null;
    }

    final var status = dto.getCourseSchedule().getStatus();
    final var statusSpan = new Span(getTranslation(status.getDisplayNameKey()));
    statusSpan.getElement().getThemeList().add(status.getBadgeLabelClassName());
    return statusSpan;
  }

  @Nonnull
  @Override
  protected CourseFilter withSearchText(@Nonnull final String searchText) {
    return CourseFilter.fromSearchText(searchText);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("course.course");
  }
}
