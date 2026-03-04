package ch.verno.common.lib.mail.placeholder;

import jakarta.annotation.Nonnull;

public enum Placeholder {
  FIRSTNAME("Firstname", "${firstname}"),
  LASTNAME("Lastname", "${lastname}"),
  COURSE_NAME("CourseName", "${courseName}"),
  COURSE_START_DATE("CourseStartDate", "${courseStartDate}"),
  COURSE_END_DATE("CourseEndDate", "${courseEndDate}")
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
