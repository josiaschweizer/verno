package ch.verno.common.db.filter;

import ch.verno.common.db.type.CourseScheduleStatus;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class CourseScheduleFilter {

  @Nullable private String searchText;
  @Nullable private CourseScheduleStatus status;
  @Nullable private Integer week;

  public CourseScheduleFilter(@Nullable final String searchText,
                              @Nullable final CourseScheduleStatus status,
                              @Nullable final Integer week) {
    this.searchText = searchText;
    this.status = status;
    this.week = week;
  }

  @Nullable
  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(@Nullable final String searchText) {
    this.searchText = searchText;
  }

  @Nullable
  public CourseScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(@Nullable final CourseScheduleStatus status) {
    this.status = status;
  }

  @Nullable
  public Integer getWeek() {
    return week;
  }

  public void setWeek(@Nullable final Integer week) {
    this.week = week;
  }

  @Nonnull
  public static CourseScheduleFilter fromSearchText(@Nullable final String searchText) {
    return new CourseScheduleFilter(searchText, null, null);
  }

  @Nonnull
  public static CourseScheduleFilter empty() {
    return new CourseScheduleFilter(null, null, null);
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (!(o instanceof CourseScheduleFilter that)) return false;
    return Objects.equals(searchText, that.searchText)
            && status == that.status
            && Objects.equals(week, that.week);
  }

  @Override
  public int hashCode() {
    return Objects.hash(searchText, status, week);
  }

  @Override
  public String toString() {
    return "CourseScheduleFilter{" +
            "searchText='" + searchText + '\'' +
            ", status=" + status +
            ", week=" + week +
            '}';
  }
}