package ch.verno.ui.base.components.schedulepicker;

import jakarta.annotation.Nonnull;

public record YearWeek(int year,
                       int week) {

  @Nonnull
  @Override
  public String toString() {
    return String.format("KW%02d-%d", week, year);
  }
}