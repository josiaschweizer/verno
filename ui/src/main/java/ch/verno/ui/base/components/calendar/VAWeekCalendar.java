package ch.verno.ui.base.components.calendar;

import ch.verno.common.lib.Routes;
import ch.verno.lib.CssConstants;
import ch.verno.lib.CssImportConstants;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.ui.lib.url.RoutesUtil;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@CssImport(CssImportConstants.VA_WEEK_CALENDAR)
public class VAWeekCalendar extends Composite<Div> {

  @NonNls public static final String WRAPPER_CLASSNAME = "va-week-calendar-wrapper";
  @NonNls public static final String TOOLBAR_CLASSNAME = "va-week-calendar-toolbar";
  @NonNls public static final String TITLE_CLASSNAME = "va-week-calendar-title";
  @NonNls public static final String STACK_CLASSNAME = "va-week-calendar-stack";
  @NonNls public static final String VA_WEEK_CALENDAR_CLASSNAME = "va-week-calendar";
  @NonNls public static final String EVENTS_CLASSNAME = "va-week-calendar-events";
  @NonNls public static final String CORNER_CLASSNAME = "va-week-calendar-corner";
  @NonNls public static final String DAY_HEADER_CLASSNAME = "va-week-calendar-day-header";
  @NonNls public static final String CALENDAR_CELL_CLASSNAME = "va-week-calendar-cell";
  @NonNls public static final String CALENDAR_HOUR_CLASSNAME = "va-week-calendar-hour";
  @NonNls public static final String COURSE_CLASSNAME = "va-week-calendar-course";
  @NonNls public static final String COURSE_TITLE_CLASSNAME = "va-week-calendar-course-title";
  @NonNls public static final String COURSE_INSTRUCTOR_CLASSNAME = "va-week-calendar-course-instructor";

  @NonNls public static final String DATA_HOUR_ATTRIBUTE = "data-hour";
  @NonNls public static final String DATA_DAY_INDEX_ATTRIBUTE = "data-day-index";

  @NonNls public static final String EVENT_COLOR_CSS_VARIABLE = "--va-event-color";

  @NonNls public static final String GRID_COLUMN_STYLE = "grid-column";
  @NonNls public static final String GRID_ROW_STYLE = "grid-row";

  private static final int HOURS = 24;

  @NonNls public static final String HOUR_FORMAT = "%02d:00";
  @Nonnull private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
  @Nonnull private static final DateTimeFormatter CELL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM", Locale.GERMAN);

  @Nonnull
  private LocalDate weekStart;

  @Nonnull
  private final Div grid;
  @Nonnull
  private final Span title;
  @Nonnull
  private final Div eventsLayer;

  @Nonnull
  private List<WeekCalendarEventDto> events;

  @Nullable
  private WeekStartChangeListener weekStartChangeListener;

  public VAWeekCalendar() {
    final var root = getContent();
    root.addClassName(WRAPPER_CLASSNAME);

    weekStart = startOfWeekMonday(LocalDate.now());
    events = List.of();

    final var toolbar = new HorizontalLayout();
    toolbar.addClassName(TOOLBAR_CLASSNAME);

    final var prev = new Button(Publ.LEFT_SINGLE_ANGLE_QUOTATION_MARK);
    final var today = new Button(getTranslation("base.today"));
    final var next = new Button(Publ.RIGHT_SINGLE_ANGLE_QUOTATION_MARK);

    title = new Span();
    title.addClassName(TITLE_CLASSNAME);

    prev.addClickListener(e -> {
      weekStart = weekStart.minusWeeks(1);
      render();
      fireWeekStartChanged();
    });

    next.addClickListener(e -> {
      weekStart = weekStart.plusWeeks(1);
      render();
      fireWeekStartChanged();
    });

    today.addClickListener(e -> {
      weekStart = startOfWeekMonday(LocalDate.now());
      render();
      fireWeekStartChanged();
    });

    toolbar.add(prev, today, next, title);

    final var stack = new Div();
    stack.addClassName(STACK_CLASSNAME);

    grid = new Div();
    grid.addClassName(VA_WEEK_CALENDAR_CLASSNAME);

    eventsLayer = new Div();
    eventsLayer.addClassName(EVENTS_CLASSNAME);

    stack.add(grid, eventsLayer);

    root.add(toolbar, stack);

    render();
  }

  private void render() {
    grid.removeAll();
    eventsLayer.removeAll();

    final var weekEnd = weekStart.plusDays(6);
    title.setText(HEADER_DATE_FORMATTER.format(weekStart) +
            Publ.SPACE + Publ.MINUS + Publ.SPACE +
            HEADER_DATE_FORMATTER.format(weekEnd));

    grid.add(createCorner());
    for (int i = 0; i < 7; i++) {
      final var date = weekStart.plusDays(i);
      grid.add(createDayHeader(date));
    }

    for (int hour = 0; hour < HOURS; hour++) {
      grid.add(createHourLabel(hour));
      for (int dayIndex = 0; dayIndex < 7; dayIndex++) {
        grid.add(createCell(hour, dayIndex));
      }
    }

    renderEvents();
  }

  @Nonnull
  private Div createCorner() {
    final var corner = new Div();
    corner.addClassName(CORNER_CLASSNAME);
    return corner;
  }

  @Nonnull
  private Div createDayHeader(@Nonnull final LocalDate date) {
    final var header = new Div();
    header.addClassName(DAY_HEADER_CLASSNAME);

    final String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.GERMAN);

    header.add(new Span(dayName + Publ.SPACE + CELL_DATE_FORMATTER.format(date)));
    return header;
  }

  @Nonnull
  private Div createHourLabel(final int hour) {
    final var label = new Div();
    label.addClassName(CALENDAR_HOUR_CLASSNAME);
    label.setText(String.format(HOUR_FORMAT, hour));
    return label;
  }

  @Nonnull
  private Div createCell(final int hour, final int dayIndex) {
    final var cell = new Div();
    cell.addClassName(CALENDAR_CELL_CLASSNAME);
    cell.getElement().setAttribute(DATA_HOUR_ATTRIBUTE, String.valueOf(hour));
    cell.getElement().setAttribute(DATA_DAY_INDEX_ATTRIBUTE, String.valueOf(dayIndex));
    return cell;
  }

  private void renderEvents() {
    for (final var layout : buildLayoutsForCurrentWeek()) {
      if (layout.event().start() == null || layout.event().end() == null) {
        continue;
      }
      eventsLayer.add(createEventBlock(layout.event(), layout.laneIndex(), layout.laneCount()));
    }
  }

  private boolean isInCurrentWeek(@Nonnull final WeekCalendarEventDto event) {
    if (event.start() == null || event.end() == null) {
      return false;
    }

    final var startDate = event.start().toLocalDate();
    final var endDate = event.end().toLocalDate();
    final var weekEnd = weekStart.plusDays(6);

    boolean startsBeforeWeekEnds = !startDate.isAfter(weekEnd);
    boolean endsAfterWeekStarts = !endDate.isBefore(weekStart);

    return startsBeforeWeekEnds && endsAfterWeekStarts;
  }

  @Nonnull
  private Div createEventBlock(@Nonnull final WeekCalendarEventDto event,
                               final int laneIndex,
                               final int laneCount) {
    final var block = new Div();
    block.addClassName(COURSE_CLASSNAME);

    final var hex = event.color();
    block.getStyle().set(EVENT_COLOR_CSS_VARIABLE, hex);

    final var titleSpan = new Span(event.title());
    titleSpan.addClassName(COURSE_TITLE_CLASSNAME);
    block.add(titleSpan);

    if (event.instructor() != null && !event.instructor().isBlank()) {
      final var instructorSpan = new Span(event.instructor());
      instructorSpan.addClassName(COURSE_INSTRUCTOR_CLASSNAME);
      block.add(instructorSpan);
    }

    block.getStyle().setCursor(CssConstants.CURSOR_POINTER);
    block.addClickListener(e -> {
      if (event.courseId() != null) {
        UI.getCurrent().navigate(RoutesUtil.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL, String.valueOf(event.courseId())));
      }
    });

    final int col = 2 + dayIndexFromMonday(event.start().getDayOfWeek());

    final int startHour = clamp(event.start().getHour(), 23);
    int endHour = clamp(event.end().getHour(), 23);

    if (event.end().toLocalTime().equals(LocalTime.MIDNIGHT) && event.end().isAfter(event.start())) {
      endHour = 23;
    }

    int rowStart = 2 + startHour;
    int rowEnd = 2 + Math.min(endHour + 1, 24);

    if (rowEnd <= rowStart) {
      rowEnd = Math.min(rowStart + 1, 26);
    }

    block.getStyle().set(GRID_COLUMN_STYLE, String.valueOf(col));
    block.getStyle().set(GRID_ROW_STYLE, rowStart + Publ.SPACE + Publ.SLASH + Publ.SPACE + rowEnd);

    final int safeLaneCount = Math.max(1, laneCount);
    final int safeLaneIndex = clamp(laneIndex, safeLaneCount - 1);

    block.getStyle().setWidth("calc((100% / " + safeLaneCount + ") - 8px)");
    block.getStyle().setMarginLeft("calc((100% / " + safeLaneCount + ") * " + safeLaneIndex + ")");
    block.getStyle().setZIndex(10 + safeLaneIndex);

    return block;
  }

  private int dayIndexFromMonday(@Nonnull final DayOfWeek dayOfWeek) {
    return dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
  }

  private int clamp(final int value, final int max) {
    return Math.clamp(max, 0, value);
  }

  @Nonnull
  private static LocalDate startOfWeekMonday(@Nonnull final LocalDate date) {
    return date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

  public void setEvents(@Nonnull final List<WeekCalendarEventDto> events) {
    this.events = events;
    render();
  }

  public void addWeekStartChangeListener(@Nonnull final WeekStartChangeListener listener) {
    weekStartChangeListener = listener;
  }

  private void fireWeekStartChanged() {
    if (weekStartChangeListener != null) {
      weekStartChangeListener.onWeekStartChanged(weekStart);
    }
  }

  @FunctionalInterface
  public interface WeekStartChangeListener {

    void onWeekStartChanged(@Nonnull final LocalDate newWeekStart);

  }

  private record EventLayout(@Nonnull WeekCalendarEventDto event, int laneIndex, int laneCount) {
  }

  private record Interval(@Nonnull LocalDateTime start, @Nonnull LocalDateTime end) {

    @Nonnull
    public static Interval empty() {
      return new Interval(LocalDateTime.MIN, LocalDateTime.MIN);
    }

  }

  @Nonnull
  private List<EventLayout> buildLayoutsForCurrentWeek() {
    final var inWeek = events.stream()
            .filter(e -> e.start() != null && e.end() != null)
            .filter(this::isInCurrentWeek)
            .toList();

    final var result = New.<EventLayout>list();

    for (int day = 0; day < 7; day++) {
      final var date = weekStart.plusDays(day);
      final var dayEvents = inWeek.stream()
              .filter(e -> intersectsDate(e, date))
              .filter(e -> e.start() != null && e.end() != null)
              .sorted(Comparator.comparing(WeekCalendarEventDto::start))
              .toList();

      result.addAll(layoutDay(date, dayEvents));
    }

    return result;
  }

  private boolean intersectsDate(@Nonnull final WeekCalendarEventDto e,
                                 @Nonnull final LocalDate date) {
    if (e.start() == null || e.end() == null) {
      return false;
    }

    final var s = e.start().toLocalDate();
    final var en = e.end().toLocalDate();
    return !date.isBefore(s) && !date.isAfter(en);
  }

  @Nonnull
  private List<EventLayout> layoutDay(@Nonnull final LocalDate day,
                                      @Nonnull final List<WeekCalendarEventDto> dayEvents) {
    final var layouts = new ArrayList<EventLayout>();
    if (dayEvents.isEmpty()) {
      return layouts;
    }

    final LocalDateTime dayStart = day.atStartOfDay();
    final LocalDateTime dayEndExclusive = day.plusDays(1).atStartOfDay();

    final var active = new ArrayList<WeekCalendarEventDto>();
    final var laneByEvent = new HashMap<WeekCalendarEventDto, Integer>();
    final var usedLanes = new BitSet();

    final var currentCluster = New.<WeekCalendarEventDto>list();
    int currentClusterPeak = 0;
    LocalDateTime currentClusterEnd = null;

    for (final var e : dayEvents) {
      final var interval = clampToDay(e, dayStart, dayEndExclusive);

      for (int i = active.size() - 1; i >= 0; i--) {
        final var a = active.get(i);
        final var aInt = clampToDay(a, dayStart, dayEndExclusive);
        if (!aInt.end.isAfter(interval.start)) {
          final int lane = laneByEvent.getOrDefault(a, -1);
          if (lane >= 0) {
            usedLanes.clear(lane);
          }
          active.remove(i);
        }
      }

      if (currentClusterEnd == null || !currentClusterEnd.isAfter(interval.start)) {
        if (!currentCluster.isEmpty()) {
          final int laneCount = Math.max(1, currentClusterPeak);
          for (final var ce : currentCluster) {
            layouts.add(new EventLayout(ce, laneByEvent.get(ce), laneCount));
          }
        }
        currentCluster.clear();
        currentClusterPeak = 0;
        currentClusterEnd = interval.end;
      } else {
        if (interval.end.isAfter(currentClusterEnd)) {
          currentClusterEnd = interval.end;
        }
      }

      int lane = usedLanes.nextClearBit(0);
      usedLanes.set(lane);
      laneByEvent.put(e, lane);

      active.add(e);
      currentCluster.add(e);

      currentClusterPeak = Math.max(currentClusterPeak, active.size());
    }

    final var laneCount = Math.max(1, currentClusterPeak);
    for (final var cluster : currentCluster) {
      layouts.add(new EventLayout(cluster, laneByEvent.get(cluster), laneCount));
    }

    return layouts;
  }

  @Nonnull
  private Interval clampToDay(@Nonnull final WeekCalendarEventDto e,
                              @Nonnull final LocalDateTime dayStart,
                              @Nonnull final LocalDateTime dayEndExclusive) {
    LocalDateTime start = e.start();
    LocalDateTime end = e.end();

    if (start == null || end == null) {
      return Interval.empty();
    }

    if (start.isBefore(dayStart)) {
      start = dayStart;
    }
    if (end.isAfter(dayEndExclusive)) {
      end = dayEndExclusive;
    }

    if (!end.isAfter(start)) {
      end = start.plusMinutes(1);
    }

    return new Interval(start, end);
  }
}