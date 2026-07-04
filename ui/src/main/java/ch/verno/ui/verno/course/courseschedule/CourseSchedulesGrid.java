package ch.verno.ui.verno.course.courseschedule;

import ch.verno.common.db.constants.course.CourseScheduleConstants;
import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.filter.CourseScheduleFilter;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.lib.VernoUtility;
import ch.verno.rpc.client.course.CourseScheduleClient;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.lib.icon.VaadinIconConstants;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.url.RoutesUtil;
import com.google.inject.Injector;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSE_SCHEDULES)
@Menu(order = 3.2, icon = VaadinIconConstants.CALENDAR, title = "courseSchedule.course.schedules")
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

    return courseScheduleClient.getCourseSchedules(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("courseSchedule.course.schedule");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return RoutesUtil.createUrlFromUrlSegments(Routes.COURSE_SCHEDULES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<CourseScheduleDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<CourseScheduleDto>>();
    columns.add(new ObjectGridColumn<>(CourseScheduleConstants.TITLE, CourseScheduleDto::getTitle, getTranslation("shared.title"), true));
    columns.add(new ObjectGridColumn<>(CourseScheduleConstants.FIRST_WEEK, dto -> !dto.getWeeks().isEmpty() ? dto.getWeeks().getFirst() : null, getTranslation("courseSchedule.first.week"), false));
    columns.add(new ObjectGridColumn<>(CourseScheduleConstants.LAST_WEEK, dto -> !dto.getWeeks().isEmpty() ? dto.getWeeks().getLast() : null, getTranslation("courseSchedule.last.week"), false));
    columns.add(new ObjectGridColumn<>(CourseScheduleConstants.WEEKS, CourseScheduleDto::getWeeksAsString, getTranslation("courseSchedule.weeks"), false));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<CourseScheduleDto>> getComponentColumns() {
    final var componentColumns = new ArrayList<ComponentGridColumn<CourseScheduleDto>>();
    componentColumns.add(new ComponentGridColumn<>(CourseScheduleConstants.COLOR, this::getColorSpan, getTranslation("shared.color"), false));
    componentColumns.add(new ComponentGridColumn<>(CourseScheduleConstants.STATUS, this::getStatusBadge, getTranslation("shared.status"), true, GridActionRoles.STICK_COLUMN));
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
            .setWidth(VernoUtility.ONE_REM)
            .setHeight(VernoUtility.ONE_REM)
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
