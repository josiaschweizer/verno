package ch.verno.ui.verno.course.courses;

import ch.verno.common.db.constants.course.CourseConstants;
import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.filter.CourseFilter;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.course.CourseClient;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.i18n.TranslationHelper;
import ch.verno.ui.lib.icon.VaadinIconConstants;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.url.RoutesUtil;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@com.vaadin.flow.router.Route(Routes.COURSES)
@Menu(order = 3.1, icon = VaadinIconConstants.DESKTOP, title = "course.course.overview")
public class CoursesGrid extends BaseOverviewGrid<CourseDto, CourseFilter> implements HasDynamicTitle {

  @Nonnull private final Lazy<CourseClient> courseClient;
  @Nonnull private final TranslationHelper translationHelper;

  public CoursesGrid(@Nonnull final Injector injector) {
    super(injector, CourseFilter.empty());

    this.courseClient = Lazy.of(() -> injector.getInstance(CourseClient.class));
    this.translationHelper = injector.getInstance(TranslationHelper.class);
  }

  @Nonnull
  @Override
  protected Stream<CourseDto> fetch(@Nonnull final Query<CourseDto, CourseFilter> query, @Nonnull final CourseFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return courseClient.get().getCourses(filter, sortOrders, offset, limit).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("course.course");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return RoutesUtil.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<CourseDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<CourseDto>>();
    columns.add(new ObjectGridColumn<>(CourseConstants.TITLE, CourseDto::getTitle, getTranslation("shared.title"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.CAPACITY, CourseDto::getCapacity, getTranslation("course.max.capacity"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.WEEKDAYS, CourseDto::getWeekdaysAsString, getTranslation("course.weekdays"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.INSTRUCTOR, CourseDto::getInstructorAsString, getTranslation("shared.instructor"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.COURSE_SCHEDULE, CourseDto::getCourseScheduleAsString, getTranslation("course.schedule"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.COURSE_LEVELS, CourseDto::getCourseLevelAsString, getTranslation("course.level"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.START_TIME, CourseDto::getStartTime, getTranslation("course.start.time"), true));
    columns.add(new ObjectGridColumn<>(CourseConstants.END_TIME, CourseDto::getEndTime, getTranslation("course.end.time"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<CourseDto>> getComponentColumns() {
    final var componentColumns = new ArrayList<ComponentGridColumn<CourseDto>>();
    componentColumns.add(new ComponentGridColumn<>(CourseConstants.STATUS, this::getStatusBadge, getTranslation("shared.status"), true, GridActionRoles.STICK_COLUMN));
    componentColumns.add(new ComponentGridColumn<>(GRID_COLUMN_ACTION_COLUMN, this::getActionContextMenuButton, getTranslation("shared.action"), false, GridActionRoles.STICK_COLUMN));
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
    return !courseClient.get().isCourseReferenced(dto);
  }

  private void delete(@Nonnull final CourseDto dto) {
    final var response = courseClient.get().delete(dto);

    if (response.successful()) {
      setFilter(getFilter()); // refresh grid by re setting filter
    } else if (response.deleteErrorCode() != null) {
      NotificationFactory.showErrorNotification(translationHelper.getTranslation(response.deleteErrorCode().getTranslationKey()));
    }
  }
}
