package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseLevelDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.report.ReportServerGate;
import ch.verno.publ.Publ;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.base.pages.grid.BaseOverviewGrid;
import ch.verno.ui.base.pages.grid.ComponentGridColumn;
import ch.verno.ui.base.pages.grid.ObjectGridColumn;
import ch.verno.ui.verno.dashboard.report.ParticipantsReportDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBoxBase;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.PARTICIPANTS)
@Menu(order = 1, icon = "vaadin:users", title = "participant.participants.overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto, ParticipantFilter> implements HasDynamicTitle {

  @Nonnull private final IParticipantService participantService;
  @Nonnull private final ICourseService courseService;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final ReportServerGate reportServerGate;

  public ParticipantsGrid(@Nonnull final GlobalInterface globalInterface,
                          final boolean showGridToolbar,
                          final boolean showFilterToolbar) {
    super(globalInterface, ParticipantFilter.empty(), showGridToolbar, showFilterToolbar);

    this.participantService = globalInterface.getService(IParticipantService.class);
    this.courseService = globalInterface.getService(ICourseService.class);
    this.courseLevelService = globalInterface.getService(ICourseLevelService.class);
    this.reportServerGate = globalInterface.getGate(ReportServerGate.class);
  }

  @Autowired
  public ParticipantsGrid(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, ParticipantFilter.empty(), true, true);

    this.participantService = globalInterface.getService(IParticipantService.class);
    this.courseService = globalInterface.getService(ICourseService.class);
    this.courseLevelService = globalInterface.getService(ICourseLevelService.class);
    this.reportServerGate = globalInterface.getGate(ReportServerGate.class);
  }

  @Nonnull
  @Override
  protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                         @Nonnull final ParticipantFilter filter) {
    final int offset = query.getOffset();
    final int limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return participantService.findParticipants(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("participant.participant");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.PARTICIPANTS, Routes.DETAIL);
  }

  @Override
  public void createContextMenu() {
    final var gridContextMenu = grid.addContextMenu();

    gridContextMenu.setDynamicContentHandler(dto -> {
      gridContextMenu.removeAll();
      if (dto == null) {
        return false;
      }

      for (final var action : buildContextMenuActions(dto)) {
        final var item = gridContextMenu.addItem(action.getComponent(), e -> action.getRunnable().run());
        item.setEnabled(action.isEnabled());
      }
      return true;
    });
  }

  @Nonnull
  @Override
  protected List<ActionDef> buildContextMenuActions(@Nonnull ParticipantDto dto) {
    final var actions = new ArrayList<ActionDef>();

    if (dto.isActive()) {
      actions.add(ActionDef.create(
              SpanFactory.createSpan(getTranslation("participant.disable.participant"), VaadinIcon.BAN),
              () -> disableItem(dto)
      ));
    } else {
      actions.add(ActionDef.create(
              SpanFactory.createSpan(getTranslation("participant.enable.participant"), VaadinIcon.CHECK_CIRCLE),
              () -> enableItem(dto)
      ));
    }

    return actions;
  }

  private void disableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(false);
    participantService.updateParticipant(dto);
    refreshGrid();
  }

  private void enableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(true);
    participantService.updateParticipant(dto);
    refreshGrid();
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<ParticipantDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<ParticipantDto>>();
    columns.add(new ObjectGridColumn<>("lastname", ParticipantDto::getLastName, getTranslation("shared.last.name"), true));
    columns.add(new ObjectGridColumn<>("firstname", ParticipantDto::getFirstName, getTranslation("shared.first.name"), true));
    columns.add(new ObjectGridColumn<>("birthdate", ParticipantDto::getAgeFromBirthday, getTranslation("shared.age"), true));
    columns.add(new ObjectGridColumn<>("email", ParticipantDto::getEmail, getTranslation("shared.e.mail"), true));
    columns.add(new ObjectGridColumn<>("phone", ParticipantDto::getPhoneString, getTranslation("shared.phone"), true));
    columns.add(new ObjectGridColumn<>("note", ParticipantDto::getNote, getTranslation("shared.note"), true));
    columns.add(new ObjectGridColumn<>("courseLevels", dto -> joinDisplayNamesFromList(dto.getCourseLevels(), CourseLevelDto::displayName),
            getTranslation("courseLevel.course_level"), true));
    columns.add(new ObjectGridColumn<>("courses", dto -> joinDisplayNamesFromList(dto.getCourses(), CourseDto::displayName),
            getTranslation("course.course"), true));
    columns.add(new ObjectGridColumn<>("parentOne", dto -> dto.getParentOne().displayName(), getTranslation("participant.parent_one"), true));
    columns.add(new ObjectGridColumn<>("parentTwo", dto -> dto.getParentTwo().displayName(), getTranslation("participant.parent_two"), true));
    columns.add(new ObjectGridColumn<>("address", dto -> dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<ParticipantDto>> getComponentColumns() {
    final var components = new ArrayList<ComponentGridColumn<ParticipantDto>>();
    components.add(new ComponentGridColumn<>("active", this::getStatusBadge, getTranslation("shared.status"), true, GridActionRoles.STICK_COLUMN));
    components.add(new ComponentGridColumn<>("actionColumn", this::getActionContextMenuButton, getTranslation("shared.action"), false, GridActionRoles.STICK_COLUMN));
    return components;
  }

  @Nonnull
  private Span getStatusBadge(@Nonnull final ParticipantDto dto) {
    final var string = dto.isActive() ? getTranslation("shared.active") : getTranslation("shared.inactive");
    final var statusSpan = new Span(string);
    statusSpan.getElement().getThemeList().add(dto.isActive() ? "badge success" : "badge error");
    return statusSpan;
  }

  @Nonnull
  private Span getActionContextMenuButton(@Nonnull final ParticipantDto dto) {
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
  private <T> String joinDisplayNamesFromList(@Nonnull final List<T> list,
                                              @Nonnull final Function<T, String> mapper) {
    if (list.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    return list.stream()
            .filter(Objects::nonNull)
            .map(mapper)
            .filter(s -> s != null && !s.isBlank())
            .distinct()
            .collect(Collectors.joining(Publ.COMMA + Publ.SPACE));
  }

  @Nonnull
  @Override
  public List<ComboBoxBase<?, ?, ?>> getFilterComponents() {
    final var courses = courseService.getAllCourses().stream()
            .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle));
    final var courseFilter = filterEntryFactory.createMultiSelectComboboxFilter(
            ParticipantFilter::getCourseIds,
            ParticipantFilter::setCourseIds,
            courses,
            filterBinder,
            getTranslation("filter.course_filter"));

    final var courseLevels = courseLevelService.getAllCourseLevels().stream()
            .collect(Collectors.toMap(BaseDto::getId, CourseLevelDto::getName));
    final var courseLevelFilter = filterEntryFactory.createMultiSelectComboboxFilter(
            ParticipantFilter::getCourseLevelIds,
            ParticipantFilter::setCourseLevelIds,
            courseLevels,
            filterBinder,
            getTranslation("filter.course_level_filter"));

    final var options = new HashMap<Long, String>();
    options.put(1L, getTranslation("shared.active"));
    options.put(0L, getTranslation("shared.inactive"));
    final var activeFilter = filterEntryFactory.createComboBoxFilter(
            ParticipantFilter::getActiveAsLong,
            ParticipantFilter::setActiveFromLong,
            options,
            filterBinder,
            getTranslation("filter.active.filter"));

    return List.of(courseFilter, courseLevelFilter, activeFilter);
  }

  @Nonnull
  @Override
  protected ViewToolbar createGridToolbar() {
    final var gridToolbar = ViewToolbarFactory.createGridToolbar(globalInterface, getGridObjectName(), getDetailPageRoute());
    final var participantReportDialogButton = new Button(getTranslation("shared.report"), VaadinIcon.FILE_TEXT.create());
    participantReportDialogButton.addClickListener(e -> createDialogButtonClick());
    gridToolbar.addActionButton(participantReportDialogButton, true);
    return gridToolbar;
  }

  private void createDialogButtonClick() {
    final var reportDialog = new ParticipantsReportDialog(reportServerGate);
    reportDialog.open();
  }

  @Nonnull
  @Override
  protected ParticipantFilter withSearchText(@Nonnull final String searchText) {
    return ParticipantFilter.fromSearchText(searchText);
  }

  @Override
  protected void setDefaultSorting() {
    final var lastNameCol = columnsByKey.get("lastname");
    if (lastNameCol == null) {
      return;
    }

    grid.sort(List.of(new GridSortOrder<>(lastNameCol, SortDirection.ASCENDING)));
  }

  @Override
  public String getPageTitle() {
    return getTranslation("participant.participants.overview");
  }
}