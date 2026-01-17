package ch.verno.ui.base.components.calendar;

import ch.verno.publ.Publ;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@CssImport("./components/va-week-calendar.css")
public class VAWeekCalendar extends Composite<Div> {

  private static final int HOURS = 24;

  @Nonnull
  private static final DateTimeFormatter HEADER_DATE_FORMATTER =
          DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.GERMAN);
  @Nonnull
  private static final DateTimeFormatter CELL_DATE_FORMATTER =
          DateTimeFormatter.ofPattern("dd.MM", Locale.GERMAN);

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
    root.addClassName("va-week-calendar-wrapper");

    weekStart = startOfWeekMonday(LocalDate.now());
    events = List.of();

    final var toolbar = new HorizontalLayout();
    toolbar.addClassName("va-week-calendar-toolbar");

    final var prev = new Button(Publ.LEFT_SINGLE_ANGLE_QUOTATION_MARK);
    final var today = new Button(getTranslation("base.today"));
    final var next = new Button(Publ.RIGHT_SINGLE_ANGLE_QUOTATION_MARK);

    title = new Span();
    title.addClassName("va-week-calendar-title");

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
    stack.addClassName("va-week-calendar-stack");

    grid = new Div();
    grid.addClassName("va-week-calendar");

    eventsLayer = new Div();
    eventsLayer.addClassName("va-week-calendar-events");

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
    corner.addClassName("va-week-calendar-corner");
    return corner;
  }

  @Nonnull
  private Div createDayHeader(@Nonnull final LocalDate date) {
    final var header = new Div();
    header.addClassName("va-week-calendar-day-header");

    final String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.GERMAN);

    header.add(new Span(dayName + Publ.SPACE + CELL_DATE_FORMATTER.format(date)));
    return header;
  }

  @Nonnull
  private Div createHourLabel(final int hour) {
    final var label = new Div();
    label.addClassName("va-week-calendar-hour");
    label.setText(String.format("%02d:00", hour));
    return label;
  }

  @Nonnull
  private Div createCell(final int hour, final int dayIndex) {
    final var cell = new Div();
    cell.addClassName("va-week-calendar-cell");
    cell.getElement().setAttribute("data-hour", String.valueOf(hour));
    cell.getElement().setAttribute("data-day-index", String.valueOf(dayIndex));
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
    block.addClassName("va-week-calendar-course");

    final var hex = event.color();
    block.getStyle().set("--va-event-color", hex);

    final var titleSpan = new Span(event.title());
    titleSpan.addClassName("va-week-calendar-course-title");
    block.add(titleSpan);

    if (event.instructor() != null && !event.instructor().isBlank()) {
      final var instructorSpan = new Span(event.instructor());
      instructorSpan.addClassName("va-week-calendar-course-instructor");
      block.add(instructorSpan);
    }

    block.getStyle().setCursor("pointer");
    block.addClickListener(e -> {
      if (event.courseId() != null) {
        UI.getCurrent().navigate(
                Routes.createUrlFromUrlSegments(Routes.COURSES, Routes.DETAIL, String.valueOf(event.courseId())));
      }
    });

    final int col = 2 + dayIndexFromMonday(event.start().getDayOfWeek());

    final int startHour = clamp(event.start().getHour(), 0, 23);
    int endHour = clamp(event.end().getHour(), 0, 23);

    if (event.end().toLocalTime().equals(LocalTime.MIDNIGHT) && event.end().isAfter(event.start())) {
      endHour = 23;
    }

    int rowStart = 2 + startHour;
    int rowEnd = 2 + Math.min(endHour + 1, 24);

    if (rowEnd <= rowStart) {
      rowEnd = Math.min(rowStart + 1, 26);
    }

    block.getStyle().set("grid-column", String.valueOf(col));
    block.getStyle().set("grid-row", rowStart + Publ.SPACE + Publ.SLASH + Publ.SPACE + rowEnd);

    final int safeLaneCount = Math.max(1, laneCount);
    final int safeLaneIndex = Math.max(0, Math.min(laneIndex, safeLaneCount - 1));

    block.getStyle().set("width", "calc((100% / " + safeLaneCount + ") - 8px)");
    block.getStyle().set("margin-left", "calc((100% / " + safeLaneCount + ") * " + safeLaneIndex + ")");
    block.getStyle().set("z-index", String.valueOf(10 + safeLaneIndex));

    return block;
  }

  private int dayIndexFromMonday(@Nonnull final DayOfWeek dayOfWeek) {
    return dayOfWeek.getValue() - DayOfWeek.MONDAY.getValue();
  }

  private int clamp(final int value, final int min, final int max) {
    return Math.max(min, Math.min(max, value));
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

    final var result = new ArrayList<EventLayout>();

    for (int day = 0; day < 7; day++) {
      final LocalDate date = weekStart.plusDays(day);

      final var dayEvents = inWeek.stream()
              .filter(e -> intersectsDate(e, date))
              .filter(e -> e.start() != null && e.end() != null)
              .sorted(Comparator.comparing(WeekCalendarEventDto::start))
              .toList();

      result.addAll(layoutDay(date, dayEvents));
    }

    return result;
  }

  private boolean intersectsDate(@Nonnull WeekCalendarEventDto e, @Nonnull LocalDate date) {
    if (e.start() == null || e.end() == null) {
      return false;
    }

    final var s = e.start().toLocalDate();
    final var en = e.end().toLocalDate();
    return !date.isBefore(s) && !date.isAfter(en);
  }

  @Nonnull
  private List<EventLayout> layoutDay(@Nonnull LocalDate day, @Nonnull List<WeekCalendarEventDto> dayEvents) {
    final var layouts = new ArrayList<EventLayout>();
    if (dayEvents.isEmpty()) {
      return layouts;
    }

    final LocalDateTime dayStart = day.atStartOfDay();
    final LocalDateTime dayEndExclusive = day.plusDays(1).atStartOfDay();

    final var active = new ArrayList<WeekCalendarEventDto>();
    final var laneByEvent = new HashMap<WeekCalendarEventDto, Integer>();
    final var usedLanes = new BitSet();

    final var currentCluster = new ArrayList<WeekCalendarEventDto>();
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

    if (!currentCluster.isEmpty()) {
      final int laneCount = Math.max(1, currentClusterPeak);
      for (final var ce : currentCluster) {
        layouts.add(new EventLayout(ce, laneByEvent.get(ce), laneCount));
      }
    }

    return layouts;
  }

  @Nonnull
  private Interval clampToDay(@Nonnull WeekCalendarEventDto e,
                              @Nonnull LocalDateTime dayStart,
                              @Nonnull LocalDateTime dayEndExclusive) {
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