package ch.verno.common.db.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class CourseScheduleDto {

  @Nullable
  private Long id;

  @Nonnull
  private Integer weekStart;

  @Nonnull
  private Integer weekEnd;

  public CourseScheduleDto() {
    this(
            0L,
            0,
            0
    );
  }

  public CourseScheduleDto(@Nullable final Long id,
                           @Nonnull final Integer weekStart,
                           @Nonnull final Integer weekEnd) {
    this.id = id;
    this.weekStart = weekStart;
    this.weekEnd = weekEnd;
  }

  public static CourseScheduleDto empty() {
    return new CourseScheduleDto();
  }

  public boolean isEmpty() {
    return this.id != null
            && this.id == 0L
            && this.weekStart == 0
            && this.weekEnd == 0;
  }

  @Nonnull
  public String displayName() {
    return "KW " + weekStart + "–" + weekEnd;
  }

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
  }

  @Nonnull
  public Integer getWeekStart() {
    return weekStart;
  }

  public void setWeekStart(@Nonnull final Integer weekStart) {
    this.weekStart = weekStart;
  }

  @Nonnull
  public Integer getWeekEnd() {
    return weekEnd;
  }

  public void setWeekEnd(@Nonnull final Integer weekEnd) {
    this.weekEnd = weekEnd;
  }
}