package ch.verno.contract.dto.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.Set;

public class CourseFilter implements BaseFilter {

  @Nullable private String searchText;
  @Nullable private Set<Long> instructorId;
  @Nullable private Set<Long> courseScheduleId;
  @Nullable private Set<Long> courseLevelId;
  @Nullable private Set<Integer> courseScheduleStatusIds;

  public CourseFilter() {
  }

  public CourseFilter(@Nullable final String searchText,
                      @Nullable final Set<Long> instructorId,
                      @Nullable final Set<Long> courseScheduleId,
                      @Nullable final Set<Long> courseLevelId,
                      @Nullable final Set<Integer> courseScheduleStatusIds) {
    this.searchText = searchText;
    this.instructorId = instructorId;
    this.courseScheduleId = courseScheduleId;
    this.courseLevelId = courseLevelId;
    this.courseScheduleStatusIds = courseScheduleStatusIds;
  }

  @Nonnull
  public static CourseFilter fromSearchText(@Nullable final String searchText) {
    return new CourseFilter(searchText, null, null, null, null);
  }

  @Nonnull
  public static CourseFilter empty() {
    return CourseFilter.fromSearchText(null);
  }

  @Nullable
  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(@Nullable final String searchText) {
    this.searchText = searchText;
  }

  @Nullable
  public Set<Long> getInstructorId() {
    return instructorId;
  }

  public void setInstructorId(@Nullable final Set<Long> instructorId) {
    this.instructorId = instructorId;
  }

  @Nullable
  public Set<Long> getCourseScheduleId() {
    return courseScheduleId;
  }

  public void setCourseScheduleId(@Nullable final Set<Long> courseScheduleId) {
    this.courseScheduleId = courseScheduleId;
  }

  @Nullable
  public Set<Long> getCourseLevelId() {
    return courseLevelId;
  }

  public void setCourseLevelId(@Nullable final Set<Long> courseLevelId) {
    this.courseLevelId = courseLevelId;
  }

  @Nullable
  public Set<Integer> getCourseScheduleStatusIds() {
    return courseScheduleStatusIds;
  }

  public void setCourseScheduleStatusIds(@Nullable final Set<Integer> courseScheduleStatusIds) {
    this.courseScheduleStatusIds = courseScheduleStatusIds;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CourseFilter that)) {
      return false;
    }
    return Objects.equals(searchText, that.searchText)
            && Objects.equals(instructorId, that.instructorId)
            && Objects.equals(courseScheduleId, that.courseScheduleId)
            && Objects.equals(courseLevelId, that.courseLevelId)
            && Objects.equals(courseScheduleStatusIds, that.courseScheduleStatusIds);
  }

}