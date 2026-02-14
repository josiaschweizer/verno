package ch.verno.common.db.dto.table;

import ch.verno.common.ui.base.components.colorpicker.Colors;
import ch.verno.common.db.dto.YearWeekDto;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseScheduleDto extends BaseDto {

  @Nonnull
  private OffsetDateTime createdAt;

  @Nonnull private String title;
  @Nonnull private CourseScheduleStatus status;
  @Nonnull private String color;
  @Nonnull private List<YearWeekDto> weeks;

  public CourseScheduleDto() {
    setId(null);
    this.createdAt = OffsetDateTime.now();
    this.title = Publ.EMPTY_STRING;
    this.status = CourseScheduleStatus.PLANNED;
    this.color = Colors.PRIMARY_COLOR;
    this.weeks = new ArrayList<>();
  }

  public CourseScheduleDto(@Nullable final Long id,
                           @Nonnull final OffsetDateTime createdAt,
                           @Nonnull final String title,
                           @Nonnull final String color,
                           @Nonnull final CourseScheduleStatus status,
                           @Nonnull final List<YearWeekDto> weeks) {
    setId(id);
    this.createdAt = createdAt;
    this.title = title;
    this.status = status;
    this.color = color;
    this.weeks = weeks;
  }

  @Nonnull
  public static CourseScheduleDto empty() {
    return new CourseScheduleDto();
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && title.isEmpty()
            && status == CourseScheduleStatus.PLANNED
            && weeks.isEmpty();
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  public void setTitle(@Nonnull final String title) {
    this.title = title;
  }

  @Nonnull
  public CourseScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(@Nonnull final CourseScheduleStatus status) {
    this.status = status;
  }

  @Nonnull
  public String getColor() {
    return color;
  }

  public void setColor(@Nonnull final String color) {
    this.color = color;
  }

  @Nonnull
  public List<YearWeekDto> getWeeks() {
    return weeks;
  }

  public void setWeeks(@Nonnull final List<YearWeekDto> weeks) {
    this.weeks = weeks;
  }

  public String displayName() {
    if (title.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    return title;
  }

  @Nonnull
  public String getWeeksAsString() {
    final var stringBuilder = new StringBuilder();

    for (final var yearWeek : weeks) {
      if (!stringBuilder.isEmpty()) {
        stringBuilder.append(Publ.COMMA + Publ.SPACE);
      }

      stringBuilder.append(yearWeek.toString());
    }

    return stringBuilder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CourseScheduleDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}