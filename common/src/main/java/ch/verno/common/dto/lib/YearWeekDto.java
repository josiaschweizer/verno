package ch.verno.common.dto.lib;

import jakarta.annotation.Nonnull;

public record YearWeekDto(int year,
                          int week) {

  @Nonnull
  @Override
  public String toString() {
    return String.format("KW%02d-%d", week, year);
  }

}