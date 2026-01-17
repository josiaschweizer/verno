package ch.verno.ui.verno.course.courses;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.db.service.ICourseService;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.base.pages.grid.BaseOverviewGrid;
import ch.verno.ui.base.pages.grid.ComponentGridColumn;
import ch.verno.ui.base.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
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

  @Nonnull private final ICourseService courseService;

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
    componentColumns.add(new ComponentGridColumn<>("status", this::getStatusBadge, getTranslation("shared.status"), true, GridActionRoles.STICK_COLUMN));
    componentColumns.add(new ComponentGridColumn<>("actionColumn", this::getActionContextMenuButton, getTranslation("shared.action"), false, GridActionRoles.STICK_COLUMN));
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
  private Span getActionContextMenuButton(@Nonnull final CourseDto dto) {
    final var button = new Button(VaadinIcon.ELLIPSIS_DOTS_V.create());
    button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

    final var menu = new ContextMenu(button);
    menu.setOpenOnClick(true);

    menu.removeAll();
    for (final var action : buildContextMenuActions(dto)) {
      final var item = menu.addItem(action.getComponent(), ev -> action.getRunnable().run());
      item.setEnabled(action.isEnabled());
    }

    return new Span(button);
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

  @Override
  public void createContextMenu() {
    final var menu = grid.addContextMenu();

    menu.setDynamicContentHandler(dto -> {
      menu.removeAll();

      if (dto == null) {
        return false;
      }

      for (final var action : buildContextMenuActions(dto)) {
        final var item = menu.addItem(action.getComponent(), e -> action.getRunnable().run());
        item.setEnabled(action.isEnabled());
      }

      return true;
    });
  }

  @Override
  protected List<ActionDef> buildContextMenuActions(@Nonnull final CourseDto dto) {
    final var actions = new ArrayList<ActionDef>();

    actions.add(ActionDef.create(
            SpanFactory.createSpan(getTranslation("shared.delete"), VaadinIcon.TRASH),
            () -> delete(dto),
            canDelete(dto)
    ));

    return actions;
  }

  private boolean canDelete(@Nonnull final CourseDto dto) {
    return courseService.canDelete(dto);
  }

  private void delete(@Nonnull final CourseDto dto) {
    final var response = courseService.delete(dto);

    if (response.success()) {
      setFilter(getFilter()); // refresh grid by re setting filter
    } else if (response.message() != null && !response.message().isBlank()) {
      NotificationFactory.showErrorNotification(response.message());
    }
  }
}
