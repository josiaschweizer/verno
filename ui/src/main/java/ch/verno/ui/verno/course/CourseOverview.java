package ch.verno.ui.verno.course;

import ch.verno.common.ui.base.components.colorpicker.Colors;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.service.ICourseScheduleService;
import ch.verno.common.db.service.ICourseService;
import ch.verno.ui.base.components.calendar.VAWeekCalendar;
import ch.verno.ui.base.components.calendar.WeekCalendarEventDto;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route(Routes.COURSE)
@Menu(order = 3, icon = "vaadin:calendar", title = "course.course")
public class CourseOverview extends VerticalLayout implements HasDynamicTitle {

  @Nonnull
  private final ICourseScheduleService courseScheduleService;
  @Nonnull
  private final ICourseService courseService;

  public CourseOverview(@Nonnull final ICourseService courseService,
                        @Nonnull final ICourseScheduleService courseScheduleService) {
    this.courseScheduleService = courseScheduleService;
    this.courseService = courseService;

    initUI();
  }

  private void initUI() {
    setSizeFull();

    final var weekCalendar = new VAWeekCalendar();

    weekCalendar.addWeekStartChangeListener(weekChange -> weekCalendar.setEvents(getEventsForWeek(weekChange)));

    add(weekCalendar);
    expand(weekCalendar);

    weekCalendar.setEvents(getEventsForWeek(
            LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    ));
  }

  @Nonnull
  private List<WeekCalendarEventDto> getEventsForWeek(@Nonnull final LocalDate weekStart) {
    final var monday = weekStart.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    final var courseScheduleByWeek = courseScheduleService.getCourseScheduleByWeek(monday);

    final var courses = new ArrayList<CourseDto>();
    for (final var courseScheduleDto : courseScheduleByWeek) {
      if (courseScheduleDto.getId() != null) {
        courses.addAll(courseService.getCoursesByCourseScheduleId(courseScheduleDto.getId()));
      }
    }

    return getEventsFromCourse(courses, monday);
  }

  @Nonnull
  private ArrayList<WeekCalendarEventDto> getEventsFromCourse(@Nonnull final List<CourseDto> courses,
                                                              @Nonnull final LocalDate monday) {
    final var events = new ArrayList<WeekCalendarEventDto>();
    for (final var course : courses) {
      final var startTime = course.getStartTime();
      final var endTime = course.getEndTime();

      if (startTime == null || endTime == null) {
        continue;
      }

      for (final var weekday : course.getWeekdays()) {
        if (weekday == null) {
          continue;
        }

        final var date = monday.plusDays(weekday.getValue() - 1L);

        final var start = LocalDateTime.of(date, startTime);
        final var end = LocalDateTime.of(date, endTime);
        final var color = course.getCourseSchedule() != null ?
                course.getCourseSchedule().getColor() :
                Colors.PRIMARY_COLOR;

        events.add(new WeekCalendarEventDto(
                course.getTitle(),
                course.getInstructorAsString(),
                start,
                end,
                course.getId(),
                color
        ));
      }
    }
    return events;
  }

  @Override
  public String getPageTitle() {
    return getTranslation("course.course");
  }
}
