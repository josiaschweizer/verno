package ch.verno.common.util;

import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.time.temporal.WeekFields;

public final class WeekKey {

  private WeekKey() {
  }

  @Nonnull
  public static String from(@Nonnull final LocalDate date) {
    var weekFields = WeekFields.ISO;

    int week = date.get(weekFields.weekOfWeekBasedYear());
    int year = date.get(weekFields.weekBasedYear());

    return String.format("KW%02d-%04d", week, year);
  }
}