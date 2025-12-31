package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.Set;

public class ParticipantFilter {

  @Nullable
  private String searchText;

  @Nullable
  private Set<Long> genderIds;

  @Nullable
  private Set<Long> courseLevelIds;

  @Nullable
  private Set<Long> instructorIds;

  @Nullable
  private Set<Long> courseIds;

  @Nullable
  private LocalDate birthDateFrom;

  @Nullable
  private LocalDate birthDateTo;

  @Nullable
  private Boolean active;

  public ParticipantFilter() {
    // default constructor
  }

  public ParticipantFilter(@Nullable final String searchText,
                           @Nullable final Set<Long> genderIds,
                           @Nullable final Set<Long> courseLevelIds,
                           @Nullable final Set<Long> instructorIds,
                           @Nullable final Set<Long> courseIds,
                           @Nullable final LocalDate birthDateFrom,
                           @Nullable final LocalDate birthDateTo,
                           @Nullable final Boolean active) {
    this.searchText = searchText;
    this.genderIds = genderIds;
    this.courseLevelIds = courseLevelIds;
    this.instructorIds = instructorIds;
    this.courseIds = courseIds;
    this.birthDateFrom = birthDateFrom;
    this.birthDateTo = birthDateTo;
    this.active = active;
  }

  @Nonnull
  public static ParticipantFilter fromSearchText(@Nullable final String searchText) {
    return fromSearchTextAndActive(searchText, null);
  }

  @Nonnull
  public static ParticipantFilter fromSearchTextAndActive(@Nullable final String searchText,
                                                          @Nullable final Boolean active) {
    return new ParticipantFilter(searchText, null, null, null, null, null, null, active);
  }

  @Nonnull
  public static ParticipantFilter empty() {
    return new ParticipantFilter(null, null, null, null, null, null, null, null);
  }

  public static ParticipantFilter emptyActive() {
    return new ParticipantFilter(null, null, null, null, null, null, null, true);
  }

  @Nullable
  public String getSearchText() {
    return searchText;
  }

  public void setSearchText(@Nullable final String searchText) {
    this.searchText = searchText;
  }

  @Nullable
  public Set<Long> getGenderIds() {
    return genderIds;
  }

  public void setGenderIds(@Nullable final Set<Long> genderIds) {
    this.genderIds = genderIds;
  }

  @Nullable
  public Set<Long> getCourseLevelIds() {
    return courseLevelIds;
  }

  public void setCourseLevelIds(@Nullable final Set<Long> courseLevelIds) {
    this.courseLevelIds = courseLevelIds;
  }

  @Nullable
  public Set<Long> getInstructorIds() {
    return instructorIds;
  }

  public void setInstructorIds(@Nullable final Set<Long> instructorIds) {
    this.instructorIds = instructorIds;
  }

  @Nullable
  public Set<Long> getCourseIds() {
    return courseIds;
  }

  public void setCourseIds(@Nullable final Set<Long> courseIds) {
    this.courseIds = courseIds;
  }

  @Nullable
  public LocalDate getBirthDateFrom() {
    return birthDateFrom;
  }

  public void setBirthDateFrom(@Nullable final LocalDate birthDateFrom) {
    this.birthDateFrom = birthDateFrom;
  }

  @Nullable
  public LocalDate getBirthDateTo() {
    return birthDateTo;
  }

  public void setBirthDateTo(@Nullable final LocalDate birthDateTo) {
    this.birthDateTo = birthDateTo;
  }

  @Nullable
  public Boolean isActive() {
    return active;
  }

  public void setActive(@Nullable final Boolean active) {
    this.active = active;
  }
}