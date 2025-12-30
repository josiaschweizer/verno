package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.Publ;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.PARTICIPANTS)
@PageTitle("Participants Overview")
@Menu(order = 1, icon = "vaadin:users", title = "Participants Overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto, ParticipantFilter> {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final CourseLevelService courseLevelService;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService,
                          @Nonnull final CourseService courseService,
                          @Nonnull final CourseLevelService courseLevelService) {
    super(ParticipantFilter.empty());

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
    return VernoConstants.PARTICIPANT;
  }

  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.PARTICIPANTS, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<ParticipantDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<ParticipantDto, Object>, String>();
    columnsMap.put(ParticipantDto::getFirstName, "First Name");
    columnsMap.put(ParticipantDto::getLastName, "Last Name");
    columnsMap.put(ParticipantDto::getBirthdate, "Age");
    columnsMap.put(ParticipantDto::getEmail, "Email");
    columnsMap.put(ParticipantDto::getPhoneString, "Phone");
    columnsMap.put(ParticipantDto::getNote, "Note");
    columnsMap.put(dto -> dto.getCourseLevel().getName(), "Course Level");
    columnsMap.put(dto -> dto.getCourse() != null ? dto.getCourse().displayName() : Publ.EMPTY_STRING, "Course");
    columnsMap.put(dto -> dto.getParentOne().displayName(), "Parent One");
    columnsMap.put(dto -> dto.getParentTwo().displayName(), "Parent Two");
    columnsMap.put(dto -> dto.getAddress().getFullAddressAsString(), "Address");
    columnsMap.put(dto -> dto.isActive() ? "Yes" : "No", "Active");
    return columnsMap;
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
            "Course Filter");

    final var courseLevels = courseLevelService.getAllCourseLevels().stream()
            .collect(Collectors.toMap(BaseDto::getId, CourseLevelDto::getName));
    final var courseLevelFilter = filterEntryFactory.createComboboxFilter(
            ParticipantFilter::getCourseLevelIds,
            ParticipantFilter::setCourseLevelIds,
            courseLevels,
            filterBinder,
            "Course Level Filter");

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
}