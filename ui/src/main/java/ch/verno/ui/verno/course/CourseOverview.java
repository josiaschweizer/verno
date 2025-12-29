package ch.verno.ui.verno.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.server.service.CourseScheduleService;
import ch.verno.server.service.CourseService;
import ch.verno.ui.base.components.calendar.VAWeekCalendar;
import ch.verno.ui.base.components.calendar.WeekCalendarEvent;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Route(Routes.COURSE)
@PageTitle("Course")
@Menu(order = 3, icon = "vaadin:calendar", title = "Course")
public class CourseOverview extends VerticalLayout {

  @Nonnull
  private final CourseScheduleService courseScheduleService;
  @Nonnull
  private final CourseService courseService;

  public CourseOverview(@Nonnull final CourseScheduleService courseScheduleService,
                        @Nonnull final CourseService courseService) {
    this.courseScheduleService = courseScheduleService;
    this.courseService = courseService;

    initUI();
  }

  private void initUI() {
    setSizeFull();

    final var weekCalendar = new VAWeekCalendar();

    weekCalendar.addWeekStartChangeListener(weekChange -> {
      weekCalendar.setEvents(getEventsForWeek(weekChange));
    });

    add(weekCalendar);
    expand(weekCalendar);

    weekCalendar.setEvents(getEventsForWeek(
            LocalDate.now().with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
    ));
  }

  @Nonnull
  private List<WeekCalendarEvent> getEventsForWeek(@Nonnull final LocalDate weekStart) {
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

  @NonNull
  private ArrayList<WeekCalendarEvent> getEventsFromCourse(@Nonnull final List<CourseDto> courses,
                                                           @Nonnull final LocalDate monday) {
    final var events = new ArrayList<WeekCalendarEvent>();
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

        events.add(new WeekCalendarEvent(course.getTitle(),
                start,
                end
        ));
      }
    }
    return events;
  }

}
