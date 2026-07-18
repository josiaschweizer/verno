package ch.verno.common.type.course.courseschedule.status;

import ch.verno.common.dto.ui.badge.BadgeLabelVariants;
import jakarta.annotation.Nonnull;

public enum CourseScheduleStatus {
  PLANNED(CourseScheduleStatusId.PLANNED, "courseSchedule.planned", BadgeLabelVariants.NORMAL),
  ACTIVE(CourseScheduleStatusId.ACTIVE, "courseSchedule.active", BadgeLabelVariants.SUCCESS),
  COMPLETED(CourseScheduleStatusId.COMPLETED, "courseSchedule.completed", BadgeLabelVariants.CONTRAST),
  ;

  @Nonnull private final Integer id;
  @Nonnull private final String displayNameKey;
  @Nonnull private final String badgeLabelClassName;

  CourseScheduleStatus(@Nonnull final Integer id,
                       @Nonnull final String displayNameKey,
                       @Nonnull final String badgeLabelClassName) {
    this.id = id;
    this.displayNameKey = displayNameKey;
    this.badgeLabelClassName = badgeLabelClassName;
  }

  @Nonnull
  public Integer getId() {
    return id;
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
