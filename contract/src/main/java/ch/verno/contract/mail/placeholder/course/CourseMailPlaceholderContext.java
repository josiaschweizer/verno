package ch.verno.contract.mail.placeholder.course;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.mail.placeholder.base.MailContext;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

public record CourseMailPlaceholderContext(@Nonnull ParticipantDto participant,
                                           @Nullable CourseScheduleDto courseSchedule,
                                           @Nullable CourseDto course) implements MailContext {

  @Override
  public ParticipantDto participant() {
    return participant;
  }

  @Override
  public CourseScheduleDto courseSchedule() {
    return courseSchedule;
  }

  @Override
  public CourseDto course() {
    return course;
  }

  @Nonnull
  public String getCourseStartDate() {
    if (courseSchedule == null || course == null) {
      return Publ.EMPTY_STRING;
    }

    final var weeks = courseSchedule.getWeeks();
    if (weeks.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var weekdays = course.getWeekdays();
    if (weekdays.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var firstWeek = weeks.getFirst();
    final var firstDayOfWeek = weekdays.getFirst();

    final var date = LocalDate.of(firstWeek.year(), 1, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, firstWeek.week())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    return date.with(TemporalAdjusters.nextOrSame(firstDayOfWeek)).format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  @Nonnull
  public String getCourseEndDate() {
    if (courseSchedule == null || course == null) {
      return Publ.EMPTY_STRING;
    }

    final var weeks = courseSchedule.getWeeks();
    if (weeks.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var weekdays = course.getWeekdays();
    if (weekdays.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var lastWeek = weeks.getLast();
    final var lastDayOfWeek = weekdays.getLast();

    final var date = LocalDate.of(lastWeek.year(), 1, 1)
            .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, lastWeek.week())
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

    return date.with(TemporalAdjusters.nextOrSame(lastDayOfWeek)).format(DateTimeFormatter.ISO_LOCAL_DATE);
  }

  @Nonnull
  public String getCourseStartTime() {
    if (course == null) {
      return Publ.EMPTY_STRING;
    }

    final var startTime = course.getStartTime();
    if (startTime == null) {
      return Publ.EMPTY_STRING;
    }

    return startTime.toString();
  }

  @Nonnull
  public String getCourseEndTime() {
    if (course == null) {
      return Publ.EMPTY_STRING;
    }

    final var endTime = course.getEndTime();
    if (endTime == null) {
      return Publ.EMPTY_STRING;
    }

    return endTime.toString();
  }
}