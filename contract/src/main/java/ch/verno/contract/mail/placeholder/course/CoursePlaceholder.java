package ch.verno.contract.mail.placeholder.course;

import ch.verno.contract.mail.placeholder.base.Placeholder;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

public enum CoursePlaceholder implements Placeholder {
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

  CoursePlaceholder(@Nonnull final String nameKey,
                    @NonNls @Nonnull final String value) {
    this.nameKey = nameKey;
    this.value = value;
  }

  @Nonnull
  @Override
  public String getNameKey() {
    return nameKey;
  }

  @Nonnull
  @Override
  public String getValue() {
    return value;
  }
}
