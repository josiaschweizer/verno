package ch.verno.common.db.enums;

import jakarta.annotation.Nonnull;

public enum CourseScheduleStatus {
  PLANNED("courseSchedule.planned"),
  ACTIVE("courseSchedule.active"),
  COMPLETED("courseSchedule.completed"),
  ;

  @Nonnull
  private final String displayNameKey;

  CourseScheduleStatus(@Nonnull final String displayNameKey) {
    this.displayNameKey = displayNameKey;
  }

  @Nonnull
  public String getDisplayNameKey() {
    return displayNameKey;
  }
}
