package ch.verno.ui.verno.course.courses.detail;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.server.service.CourseService;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailView;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;
import java.util.stream.Collectors;

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

  public CourseDetail(@Nonnull final CourseService courseService,
                      @Nonnull final InstructorService instructorService,
                      @Nonnull final CourseLevelService courseLevelService,
                      @Nonnull final CourseScheduleService courseScheduleService) {
    this.courseService = courseService;
    this.instructorService = instructorService;
    this.courseLevelService = courseLevelService;
    this.courseScheduleService = courseScheduleService;

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
            Optional.of(getTranslation("shared.title.is.required")),
            getTranslation("shared.title"));

    //todo default value from user settings
    final var capacityEntry = entryFactory.createNumberEntry(
            courseDto -> courseDto.getCapacity() != null ? courseDto.getCapacity().doubleValue() : null,
            (dto, value) -> dto.setCapacity(value == null ? null : value.intValue()),
            getBinder(),
            Optional.empty(),
            getTranslation("course.max.capacity"));
    final var location = entryFactory.createTextEntry(
            CourseDto::getLocation,
            CourseDto::setLocation,
            getBinder(),
            Optional.empty(),
            getTranslation("course.location"));

    final var startTime = entryFactory.createTimeEntry(
            CourseDto::getStartTime,
            CourseDto::setStartTime,
            getBinder(),
            Optional.empty(),
            getTranslation("course.start.time"));
    final var endTime = entryFactory.createTimeEntry(
            CourseDto::getEndTime,
            CourseDto::setEndTime,
            getBinder(),
            Optional.empty(),
            getTranslation("course.end.time"));

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
            Optional.of(getTranslation("courseSchedule.course.schedule.is.required")),
            getTranslation("courseSchedule.course.schedule"),
            courseScheduleOptions
    );


    final var courseLevelEntry = entryFactory.createMultiSelectComboBoxEntry(
            CourseDto::getCourseLevels,
            CourseDto::setCourseLevels,
            getBinder(),
            Optional.of(getTranslation("courseLevel.at.least.one.course.level.is.required")),
            getTranslation("courseLevel.course_levels"),
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
            Optional.of(getTranslation("course.at.least.one.weekday.is.required")),
            getTranslation("course.weekdays")
    );

    return createLayoutFromComponents(weekOptions);
  }
}
