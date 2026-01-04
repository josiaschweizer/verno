package ch.verno.common.db.enums;

import jakarta.annotation.Nonnull;

public enum CourseScheduleStatus {
  PLANNED("courseSchedule.planned", "badge"),
  ACTIVE("courseSchedule.active", "badge success"),
  COMPLETED("courseSchedule.completed", "badge contrast"),
  ;

  @Nonnull
  private final String displayNameKey;
  @Nonnull
  private final String badgeLabelClassName;

  CourseScheduleStatus(@Nonnull final String displayNameKey,
                       @Nonnull final String badgeLabelClassName) {
    this.displayNameKey = displayNameKey;
    this.badgeLabelClassName = badgeLabelClassName;
  }

  @Nonnull
  public String getDisplayNameKey() {
    return displayNameKey;
  }

  @Nonnull
  public String getBadgeLabelClassName() {
    return badgeLabelClassName;
  }
}
