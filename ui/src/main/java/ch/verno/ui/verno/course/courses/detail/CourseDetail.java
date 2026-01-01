package ch.verno.ui.verno.course.courses.detail;

import ch.verno.common.db.dto.*;
import ch.verno.common.db.filter.ParticipantFilter;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.*;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.COURSES + Routes.DETAIL)
@PageTitle("Courses")
@Menu(order = 3.11, icon = "vaadin:mobile", title = "Course Detail")
public class CourseDetail extends BaseDetailView<CourseDto> {

  @Nonnull
  private final CourseService courseService;
  @Nonnull
  private final InstructorService instructorService;
  @Nonnull
  private final CourseLevelService courseLevelService;
  @Nonnull
  private final CourseScheduleService courseScheduleService;
  @Nonnull
  private final ParticipantService participantService;

  public CourseDetail(@Nonnull final CourseService courseService,
                      @Nonnull final InstructorService instructorService,
                      @Nonnull final CourseLevelService courseLevelService,
                      @Nonnull final CourseScheduleService courseScheduleService,
                      @Nonnull final ParticipantService participantService) {
    this.courseService = courseService;
    this.instructorService = instructorService;
    this.courseLevelService = courseLevelService;
    this.courseScheduleService = courseScheduleService;
    this.participantService = participantService;

    init();
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return VernoConstants.COURSE;
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.COURSES;
  }

  @Nonnull
  @Override
  protected Binder<CourseDto> createBinder() {
    return new Binder<>(CourseDto.class);
  }

  @Nonnull
  @Override
  protected CourseDto createBean(@Nonnull final CourseDto bean) {
    return courseService.createCourse(bean);
  }

  @Nonnull
  @Override
  protected CourseDto updateBean(@Nonnull final CourseDto bean) {
    return courseService.updateCourse(bean);
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }

  @Nonnull
  @Override
  protected CourseDto newBeanInstance() {
    return new CourseDto();
  }

  @Override
  protected CourseDto getBeanById(@Nonnull final Long id) {
    return courseService.getCourseById(id);
  }

  @Override
  protected void initUI() {
    final var infoPanel = createInfoLayout();
    final var coursePanel = createCourseLayout();
    final var datePanel = createDateLayout();
    add(new VerticalLayout(infoPanel, coursePanel, datePanel));
  }

  @Nonnull
  private HorizontalLayout createInfoLayout() {
    final var titleEntry = entryFactory.createTextEntry(
            CourseDto::getTitle,
            CourseDto::setTitle,
            getBinder(),
            Optional.of("Title is required"),
            "Title");

    //todo default value from user settings
    final var capacityEntry = entryFactory.createNumberEntry(
            courseDto -> courseDto.getCapacity() != null ? courseDto.getCapacity().doubleValue() : null,
            (dto, value) -> dto.setCapacity(value == null ? null : value.intValue()),
            getBinder(),
            Optional.empty(),
            "max Capacity");
    final var location = entryFactory.createTextEntry(
            CourseDto::getLocation,
            CourseDto::setLocation,
            getBinder(),
            Optional.empty(),
            "Location");

    final var startTime = entryFactory.createTimeEntry(
            CourseDto::getStartTime,
            CourseDto::setStartTime,
            getBinder(),
            Optional.empty(),
            "Start Time");
    final var endTime = entryFactory.createTimeEntry(
            CourseDto::getEndTime,
            CourseDto::setEndTime,
            getBinder(),
            Optional.empty(),
            "End Time");

    return createLayoutFromComponents(titleEntry, capacityEntry, location, startTime, endTime);
  }

  private HorizontalLayout createCourseLayout() {
    final var courseSchedules = courseScheduleService.getAllCourseSchedules();
    final var courseScheduleOptions = courseSchedules.stream()
            .collect(Collectors.toMap(CourseScheduleDto::getId, CourseScheduleDto::getTitle));

    final var courseScheduleEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getCourseSchedule() != null ? dto.getCourseSchedule().getId() : null,
            (dto, value) -> dto.setCourseSchedule(value == null ?
                    null :
                    courseScheduleService.getCourseScheduleById(value)),
            getBinder(),
            Optional.of("Course Schedule is required"),
            "Course Schedule",
            courseScheduleOptions
    );


    final var courseLevelEntry = entryFactory.createMultiSelectComboBoxEntry(
            CourseDto::getCourseLevels,
            CourseDto::setCourseLevels,
            getBinder(),
            Optional.of("At least one course level is required"),
            "Course Levels",
            courseLevelService.getAllCourseLevels(),
            CourseLevelDto::displayName
    );

    final var instructors = instructorService.getAllInstructors();
    final var instructorOptions = instructors.stream()
            .collect(Collectors.toMap(InstructorDto::getId, InstructorDto::displayName));

    final var instructorEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getInstructor() != null ? dto.getInstructor().getId() : null,
            (dto, value) -> dto.setInstructor(value == null ?
                    null :
                    instructorService.getInstructorById(value)),
            getBinder(),
            Optional.empty(),
            "Instructor",
            instructorOptions
    );

    return createLayoutFromComponents(courseScheduleEntry, courseLevelEntry, instructorEntry);
  }

  @Nonnull
  private HorizontalLayout createDateLayout() {
    final var weekOptions = entryFactory.createWeekOptionEntry(
            CourseDto::getWeekdays,
            CourseDto::setWeekdays,
            getBinder(),
            Optional.of("At least one weekday is required"),
            "Weekdays"
    );

    return createLayoutFromComponents(weekOptions);
  }

  @Override
  protected void initAdditionalInfoUIBelowSaveButton() {
    final var title = new Span("Participants in this Course");
    title.getStyle().setFontWeight("bold");

    final var participantsGrid = new ParticipantsGrid(participantService, courseService, courseLevelService, false, false) {

      @Nonnull
      @Override
      protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                             @Nonnull final ParticipantFilter filter) {
        if (getBinder().getBean() != null && getBinder().getBean().getId() != null) {
          filter.setCourseIds(Set.of(getBinder().getBean().getId()));
        }
        return super.fetch(query, filter);
      }
    };

    addOnLayout = new VerticalLayout(title, participantsGrid);
    addOnLayout.setWidthFull();
    addOnLayout.setHeightFull();

    add(addOnLayout);
  }
}
