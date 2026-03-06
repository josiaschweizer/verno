package ch.verno.common.lib.mail.placeholder;

import jakarta.annotation.Nonnull;

public enum Placeholder {
  FIRSTNAME("shared.first.name", "${firstname}"),
  LASTNAME("shared.last.name", "${lastname}"),
  COURSE_NAME("shared.course.name", "${courseName}"),
  COURSE_START_DATE("shared.course.start.date", "${courseStartDate}"),
  COURSE_END_DATE("shared.course.end.date", "${courseEndDate}"),
  COURSE_START_TIME("shared.course.start.time", "${courseStartTime}"),
  COURSE_END_TIME("shared.course.end.time", "${courseEndTime}"),
  ;

  @Nonnull private final String nameKey;
  @Nonnull private final String value;

  Placeholder(@Nonnull final String nameKey,
              @Nonnull final String value) {
    this.nameKey = nameKey;
    this.value = value;
  }

  @Nonnull
  public String getNameKey() {
    return nameKey;
  }

  @Nonnull
  public String getValue() {
    return value;
  }
}
