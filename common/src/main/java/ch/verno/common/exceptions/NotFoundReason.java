package ch.verno.common.exceptions;

import jakarta.annotation.Nonnull;

public enum NotFoundReason {
  INSTRUCTOR_BY_ID_NOT_FOUND("Instructor not found with id: "),
  PARTICIPANT_BY_ID_NOT_FOUND("Participant not found with id: "),
  ADDRESS_BY_ID_NOT_FOUND("Address not found with id: "),
  PARENT_BY_ID_NOT_FOUND("Parent not found with id: "),
  GENDER_BY_ID_NOT_FOUND("Gender not found with id: "),
  COURSE_BY_ID_NOT_FOUND("Course not found with id: "),
  COURSE_LEVEL_BY_ID_NOT_FOUND("Course not found with id: "),
  COURSE_SCHEDULE_BY_ID_NOT_FOUND("Course schedule not found with id: "),
  MANDANT_SETTINGS_BY_ID_NOT_FOUND("Mandant settings not found with id: ")
  ;

  @Nonnull
  private final String message;

  NotFoundReason(@Nonnull final String message) {
    this.message = message;
  }

  @Nonnull
  public String getMessage() {
    return message;
  }
}
