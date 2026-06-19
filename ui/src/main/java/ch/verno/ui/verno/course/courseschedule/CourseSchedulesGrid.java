package ch.verno.ui.verno.course.courseschedule;

import ch.verno.rpc.client.course.CourseScheduleClient;
import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import com.google.inject.Injector;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@com.vaadin.flow.router.Route(Routes.COURSE_SCHEDULES)
@Menu(order = 3.2, icon = "vaadin:calendar", title = "courseSchedule.course.schedules")
public class CourseSchedulesGrid extends BaseOverviewGrid<CourseScheduleDto, CourseScheduleFilter> implements HasDynamicTitle {

  @Nonnull private final CourseScheduleClient courseScheduleClient;

  @Autowired
  public CourseSchedulesGrid(@Nonnull final Injector injector) {
    this(injector, true, true);
  }

  public CourseSchedulesGrid(@Nonnull final Injector injector,
                             final boolean showGridToolbar,
                             final boolean showFilterToolbar) {
    super(injector, CourseScheduleFilter.empty(), showGridToolbar, showFilterToolbar);
    this.courseScheduleClient = injector.getInstance(CourseScheduleClient.class);
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
    columns.add(new ObjectGridColumn<>("first-week", dto -> !dto.getWeeks().isEmpty() ? dto.getWeeks().getFirst() : null, getTranslation("courseSchedule.first.week"), false));
    columns.add(new ObjectGridColumn<>("last-week", dto -> !dto.getWeeks().isEmpty() ? dto.getWeeks().getLast() : null, getTranslation("courseSchedule.last.week"), false));
    columns.add(new ObjectGridColumn<>("weeks", CourseScheduleDto::getWeeksAsString, getTranslation("courseSchedule.weeks"), false));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<CourseScheduleDto>> getComponentColumns() {
    final var componentColumns = new ArrayList<ComponentGridColumn<CourseScheduleDto>>();
    componentColumns.add(new ComponentGridColumn<>("color", this::getColorSpan, getTranslation("shared.color"), false, null));
    componentColumns.add(new ComponentGridColumn<>("status", this::getStatusBadge, getTranslation("shared.status"), true, GridActionRoles.STICK_COLUMN));
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
  private Span getColorSpan(@Nonnull final CourseScheduleDto dto) {
    final var hexColor = dto.getColor();

    final var span = new Span();
    span.getStyle()
            .setBackgroundColor(hexColor)
            .setWidth("1rem")
            .setHeight("1rem")
            .setBorderRadius("50%")
            .setDisplay(Style.Display.INLINE_BLOCK);

    return span;
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
