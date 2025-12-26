package ch.verno.ui.verno.course.detail;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.CourseLevelService;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.detail.BaseDetailPage;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.stream.Collectors;

@Route(Routes.COURSES + Routes.DETAIL)
@PageTitle("Courses")
@Menu(order = 3.1, icon = "vaadin:mobile", title = "Course Detail")
public class CourseDetailPage extends BaseDetailPage<CourseDto> {

  @Nonnull
  private final CourseService courseService;
  private final CourseLevelService courseLevelService;
  private final CourseScheduleService courseScheduleService;

  public CourseDetailPage(@Nonnull final CourseService courseService,
                          @Nonnull final CourseLevelService courseLevelService, final CourseScheduleService courseScheduleService) {
    this.courseService = courseService;
    this.courseLevelService = courseLevelService;
    this.courseScheduleService = courseScheduleService;
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
    add(new VerticalLayout(infoPanel, coursePanel));
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
            courseDto -> courseDto.getCapacity() != null ?
                    courseDto.getCapacity().doubleValue() :
                    0.0,
            (dto, value) -> dto.setCapacity(value.intValue()),
            getBinder(),
            Optional.of("Capacity is required"),
            "max Capacity");
    final var location = entryFactory.createTextEntry(
            CourseDto::getLocation,
            CourseDto::setLocation,
            getBinder(),
            Optional.empty(),
            "Location");
    //todo default value from user settings
    final var duration = entryFactory.createNumberEntry(
            courseDto -> courseDto.getDuration() != null ?
                    courseDto.getDuration().doubleValue() :
                    0.0,
            (dto, value) -> dto.setDuration(value.intValue()),
            getBinder(),
            Optional.of("Duration is required"),
            "Duration (minutes)");

    return createLayoutFromComponents(titleEntry, capacityEntry, location, duration);
  }

  private HorizontalLayout createCourseLayout() {
    final var courseSchedules = courseScheduleService.getAllCourseSchedules();
    final var courseScheduleOptions = courseSchedules.stream()
            .collect(Collectors.toMap(CourseScheduleDto::getId, CourseScheduleDto::getTitle));

    final var courseScheduleEntry = entryFactory.createComboBoxEntry(
            dto -> dto.getCourseSchedule().getId(),
            (dto, value) -> dto.setCourseSchedule(value == null ?
                    CourseScheduleDto.empty() :
                    courseScheduleService.getCourseScheduleById(value)),
            getBinder(),
            Optional.of("Course Schedule is required"),
            "Course Schedule",
            courseScheduleOptions
    );


    final var courseLevels = courseLevelService.getAllCourseLevels();
    final var courseLevelOptions = courseLevels.stream()
            .collect(Collectors.toMap(CourseLevelDto::getId, CourseLevelDto::getName));

    final var courseLevelEntry = fieldFactory.createCourseLevelField(
            dto -> dto.getCourseLevel().getId(),
            (dto, value) -> dto.setCourseLevel(value == null ?
                    CourseLevelDto.empty() :
                    courseLevelService.getCourseLevelById(value)),
            getBinder(),
            courseLevelOptions
    );

    final var weekOptions = entryFactory.createWeekOptionEntry(
            CourseDto::getWeekdays,
            CourseDto::setWeekdays,
            getBinder(),
            Optional.of("At least one weekday is required"),
            "Weekdays"
    );

    return createLayoutFromComponents(courseScheduleEntry, courseLevelEntry, weekOptions);
  }
}
