package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.Publ;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.base.grid.ComponentGridColumn;
import ch.verno.ui.base.grid.ObjectGridColumn;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.PARTICIPANTS)
@Menu(order = 1, icon = "vaadin:users", title = "participant.participants.overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto, ParticipantFilter> implements HasDynamicTitle {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final CourseLevelService courseLevelService;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService,
                          @Nonnull final CourseService courseService,
                          @Nonnull final CourseLevelService courseLevelService,
                          final boolean showGridToolbar,
                          final boolean showFilterToolbar) {
    super(ParticipantFilter.empty(), showGridToolbar, showFilterToolbar);

    this.participantService = participantService;
    this.courseService = courseService;
    this.courseLevelService = courseLevelService;
  }

  @Autowired
  public ParticipantsGrid(@Nonnull final ParticipantService participantService,
                          @Nonnull final CourseService courseService,
                          @Nonnull final CourseLevelService courseLevelService) {
    super(ParticipantFilter.empty(), true, true);

    this.participantService = participantService;
    this.courseService = courseService;
    this.courseLevelService = courseLevelService;
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

  @Override
  protected int count(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                      @Nonnull final ParticipantFilter filter) {
    return participantService.countParticipants(filter);
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
    final var menu = grid.addContextMenu();

    menu.setDynamicContentHandler(dto -> {
      menu.removeAll();

      if (dto == null) {
        return false;
      }

      if (dto.isActive()) {
        menu.addItem(
                getTranslation(getTranslation("participant.disable.participant")),
                e -> disableItem(dto)
        );
      } else {
        menu.addItem(
                getTranslation(getTranslation("participant.enable.participant")),
                e -> enableItem(dto)
        );
      }

      return true;
    });
  }

  private void disableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(false);
    participantService.updateParticipant(dto);
    grid.getDataProvider().refreshAll();
  }

  private void enableItem(@Nonnull final ParticipantDto dto) {
    dto.setActive(true);
    participantService.updateParticipant(dto);
    grid.getDataProvider().refreshAll();
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<ParticipantDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<ParticipantDto>>();
    columns.add(new ObjectGridColumn<>("firstname", ParticipantDto::getFirstName, getTranslation("shared.first.name"), true));
    columns.add(new ObjectGridColumn<>("lastname", ParticipantDto::getLastName, getTranslation("shared.last.name"), true));
    columns.add(new ObjectGridColumn<>("birthdate", ParticipantDto::getAgeFromBirthday, getTranslation("shared.age"), true));
    columns.add(new ObjectGridColumn<>("email", ParticipantDto::getEmail, getTranslation("shared.e.mail"), true));
    columns.add(new ObjectGridColumn<>("phone", ParticipantDto::getPhoneString, getTranslation("shared.phone"), true));
    columns.add(new ObjectGridColumn<>("note", ParticipantDto::getNote, getTranslation("participant.note"), true));
    columns.add(new ObjectGridColumn<>("courseLevels", dto -> joinDisplayNamesFromList(dto.getCourseLevels(), CourseLevelDto::displayName),
            getTranslation("courseLevel.course_level"), true));
    columns.add(new ObjectGridColumn<>("courses", dto -> joinDisplayNamesFromList(dto.getCourses(), CourseDto::displayName),
            getTranslation("course.course"), true));
    columns.add(new ObjectGridColumn<>("parent one", dto -> dto.getParentOne().displayName(), getTranslation("participant.parent_one"), false));
    columns.add(new ObjectGridColumn<>("parent two", dto -> dto.getParentTwo().displayName(), getTranslation("participant.parent_two"), false));
    columns.add(new ObjectGridColumn<>("address", dto -> dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<ParticipantDto>> getComponentColumns() {
    final var components = new ArrayList<ComponentGridColumn<ParticipantDto>>();
    components.add(new ComponentGridColumn<>("status", this::getStatusBadge, getTranslation("shared.status"), false));
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
            .collect(Collectors.joining(", "));
  }

  @Nonnull
  @Override
  public List<MultiSelectComboBox<Long>> getFilterComponents() {
    final var courses = courseService.getAllCourses().stream()
            .collect(Collectors.toMap(CourseDto::getId, CourseDto::getTitle));

    final var courseFilter = filterEntryFactory.createComboboxFilter(
            ParticipantFilter::getCourseIds,
            ParticipantFilter::setCourseIds,
            courses,
            filterBinder,
            getTranslation("filter.course_filter"));

    final var courseLevels = courseLevelService.getAllCourseLevels().stream()
            .collect(Collectors.toMap(BaseDto::getId, CourseLevelDto::getName));
    final var courseLevelFilter = filterEntryFactory.createComboboxFilter(
            ParticipantFilter::getCourseLevelIds,
            ParticipantFilter::setCourseLevelIds,
            courseLevels,
            filterBinder,
            getTranslation("filter.course_level_filter"));

    //todo create active filter
//    final var activeFilter = filterEntryFactory.createBooleanFilter(
//            ParticipantFilter::getActive,
//            ParticipantFilter::setActive,
//            filterBinder,
//            "Active Filter");

    return List.of(courseFilter, courseLevelFilter);
  }

  @Nonnull
  @Override
  protected ParticipantFilter withSearchText(@Nonnull final String searchText) {
    return ParticipantFilter.fromSearchText(searchText);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("participant.participants.overview");
  }
}