package ch.verno.application.course.command;

import jakarta.annotation.Nonnull;

import java.util.List;

public record CreateCourseUnitCommand(
    @Nonnull String name,
    @Nonnull List<Week> weeks
) {
  public record Week(int year, int calendarWeek) {
  }
}