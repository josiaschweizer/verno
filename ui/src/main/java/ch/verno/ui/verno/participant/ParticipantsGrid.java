package ch.verno.ui.verno.participant;

import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.lang.EmptyUtil;
import ch.verno.rpc.client.course.CourseClient;
import ch.verno.rpc.client.course.CourseLevelClient;
import ch.verno.rpc.client.file.ReportClient;
import ch.verno.rpc.client.participant.ParentClient;
import ch.verno.rpc.client.participant.ParticipantClient;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.grid.GridActionRoles;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.lib.icon.VaadinIconConstants;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.url.RoutesUtil;
import ch.verno.ui.verno.dashboard.report.ParticipantsReportDialog;
import com.google.inject.Injector;
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
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@com.vaadin.flow.router.Route(Routes.PARTICIPANTS)
@Menu(order = 1, icon = VaadinIconConstants.USER, title = "participant.participants.overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto, ParticipantFilter> implements HasDynamicTitle {

  @NonNls public static final String GRID_COLUMN_FIRSTNAME = "firstname";
  @NonNls public static final String GRID_COLUMN_LASTNAME = "lastname";
  @NonNls public static final String GRID_COLUMN_BIRTHDATE = "birthdate";
  @NonNls public static final String GRID_COLUMN_COURSE_LEVELS = "courseLevels";
  @NonNls public static final String GRID_COLUMN_SIBLINGS = "siblings";
  @NonNls public static final String GRID_COLUMN_NOTE = "note";
  @NonNls public static final String GRID_COLUMN_EMAIL = "email";
  @NonNls public static final String GRID_COLUMN_PHONE = "phone";
  @NonNls public static final String GRID_COLUMN_COURSES = "courses";
  @NonNls public static final String GRID_COLUMN_PARENT_ONE = "parentOne";
  @NonNls public static final String GRID_COLUMN_PARENT_TWO = "parentTwo";
  @NonNls public static final String GRID_COLUMN_ADDRESS = "address";

  @Nonnull private final Lazy<ReportClient> reportClient;
  @Nonnull private final Lazy<ParentClient> parentClient;
  @Nonnull private final Lazy<CourseClient> courseService;
  @Nonnull private final Lazy<ParticipantClient> participantClient;
  @Nonnull private final Lazy<CourseLevelClient> courseLevelClient;

  public ParticipantsGrid(@Nonnull final Injector injector,
                          final boolean showGridToolbar,
                          final boolean showFilterToolbar) {
    super(injector, ParticipantFilter.empty(), showGridToolbar, showFilterToolbar);

    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));
    this.parentClient = Lazy.of(() -> injector.getInstance(ParentClient.class));
    this.participantClient = Lazy.of(() -> injector.getInstance(ParticipantClient.class));
    this.courseLevelClient = Lazy.of(() -> injector.getInstance(CourseLevelClient.class));
    this.courseService = Lazy.of(() -> injector.getInstance(CourseClient.class));
  }

  @Autowired
  public ParticipantsGrid(@Nonnull final Injector injector) {
    super(injector, ParticipantFilter.empty(), true, true);

    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));
    this.parentClient = Lazy.of(() -> injector.getInstance(ParentClient.class));
    this.courseService = Lazy.of(() -> injector.getInstance(CourseClient.class));
    this.participantClient = Lazy.of(() -> injector.getInstance(ParticipantClient.class));
    this.courseLevelClient = Lazy.of(() -> injector.getInstance(CourseLevelClient.class));
  }

  @Nonnull
  @Override
  protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                         @Nonnull final ParticipantFilter filter) {
    final int offset = query.getOffset();
    final int limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return participantClient.get().getAllParticipants(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("participant.participant");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return RoutesUtil.createUrlFromUrlSegments(Routes.PARTICIPANTS, Routes.DETAIL);
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
  protected List<ActionDef> buildContextMenuActions(@Nonnull final ParticipantDto dto) {
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

    actions.add(ActionDef.create(getTranslation("participant.delete.participant"), VaadinIcon.TRASH, () -> deleteParticipant(dto)));

    return actions;
  }

  private void deleteParticipant(@Nonnull final ParticipantDto dto) {
    if (participantClient.get().deleteParticipantById(dto.getId())) {
      deleteParent(dto.getParentOne());
      deleteParent(dto.getParentTwo());

      refreshGrid();
    }
  }

  private void deleteParent(@Nonnull final ParentDto dto) {
    final var id = dto.getId();
    if (EmptyUtil.isPositive(id)) {
      parentClient.get().deleteParentById(id);
    }
  }

  private void disableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(false);
    participantClient.get().saveParticipant(dto);
    refreshGrid();
  }

  private void enableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(true);
    participantClient.get().saveParticipant(dto);
    refreshGrid();
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<ParticipantDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<ParticipantDto>>();
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_FIRSTNAME, ParticipantDto::getFirstName,
            getTranslation("shared.first.name"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_LASTNAME, ParticipantDto::getLastName,
            getTranslation("shared.last.name"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_BIRTHDATE, ParticipantDto::getAgeFromBirthday,
            getTranslation("shared.age"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_COURSE_LEVELS, dto ->
            joinDisplayNamesFromList(dto.getCourseLevels(), CourseLevelDto::displayName),
            getTranslation("courseLevel.course_level"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_SIBLINGS, dto ->
            joinDisplayNamesFromList(dto.getSiblings(), ParticipantDto::getDisplayName),
            getTranslation("participant.geschwister"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_NOTE, ParticipantDto::getNote,
            getTranslation("shared.note"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_EMAIL, ParticipantDto::getEmail,
            getTranslation("shared.e.mail"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_PHONE, ParticipantDto::getPhoneString,
            getTranslation("shared.phone"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_COURSES, dto ->
            joinDisplayNamesFromList(dto.getCourses(), CourseDto::displayName),
            getTranslation("course.course"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_PARENT_ONE, dto ->
            dto.getParentOne().displayName(), getTranslation("participant.parent_one"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_PARENT_TWO, dto ->
            dto.getParentTwo().displayName(), getTranslation("participant.parent_two"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_ADDRESS, dto ->
            dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"), true));
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
    final var courses = courseService.get().getAllCourses()
            .stream()
            .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle));
    final var courseFilter = filterEntryFactory.createMultiSelectComboboxFilter(
            ParticipantFilter::getCourseIds,
            ParticipantFilter::setCourseIds,
            courses,
            filterBinder,
            getTranslation("filter.course_filter"));

    final var courseLevels = courseLevelClient.get().getAllCourseLevels().stream()
            .collect(Collectors.toMap(
                    BaseDto::getId,
                    CourseLevelDto::getName,
                    (a, b) -> a,
                    LinkedHashMap::new
            ));
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
    final var participantReportDialogButton = new VAButton(getTranslation("shared.report"), VaadinIcon.FILE_TEXT.create());
    participantReportDialogButton.addClickListener(e -> createDialogButtonClick());

    final var gridToolbar = ViewToolbarFactory.createGridToolbar(injector, getGridObjectName(), getDetailPageRoute());
    gridToolbar.addActionButton(participantReportDialogButton, true);
    return gridToolbar;
  }

  private void createDialogButtonClick() {
    final var reportDialog = injector.getInstance(ParticipantsReportDialog.class);
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