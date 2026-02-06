package ch.verno.common.db.dto.table;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class TenantSettingDto extends BaseDto {

  @Nonnull private Integer courseDaysPerSchedule;
  @Nonnull private Integer maxParticipantsPerCourse;
  private boolean enforceQuantitySettings;
  private boolean enforceCourseLevelSettings;
  private boolean isParentOneMainParent;
  @Nonnull private String courseReportName;
  private boolean limitCourseAssignmentsToActive;

  public TenantSettingDto() {
    this(null, Publ.ZERO, Publ.ZERO, false, false, true, Publ.EMPTY_STRING, false);
  }

  public TenantSettingDto(@Nullable final Long id,
                          @Nonnull final Integer courseDaysPerSchedule,
                          @Nonnull final Integer maxParticipantsPerCourse,
                          final boolean enforceQuantitySettings,
                          final boolean enforceCourseLevelSettings,
                          final boolean isParentOneMainParent,
                          @Nonnull final String courseReportName,
                          final boolean limitCourseAssignmentsToActive) {
    setId(id);
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
    this.enforceQuantitySettings = enforceQuantitySettings;
    this.enforceCourseLevelSettings = enforceCourseLevelSettings;
    this.isParentOneMainParent = isParentOneMainParent;
    this.courseReportName = courseReportName;
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }

  @Nonnull
  public Integer getCourseDaysPerSchedule() {
    return courseDaysPerSchedule;
  }

  public void setCourseDaysPerSchedule(@Nonnull final Integer courseDaysPerSchedule) {
    this.courseDaysPerSchedule = courseDaysPerSchedule;
  }

  @Nonnull
  public Integer getMaxParticipantsPerCourse() {
    return maxParticipantsPerCourse;
  }

  public void setMaxParticipantsPerCourse(@Nonnull final Integer maxParticipantsPerCourse) {
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
  }

  public boolean isEnforceQuantitySettings() {
    return enforceQuantitySettings;
  }

  public void setEnforceQuantitySettings(final boolean enforceQuantitySettings) {
    this.enforceQuantitySettings = enforceQuantitySettings;
  }

  public boolean isEnforceCourseLevelSettings() {
    return enforceCourseLevelSettings;
  }

  public void setEnforceCourseLevelSettings(final boolean enforceCourseLevelSettings) {
    this.enforceCourseLevelSettings = enforceCourseLevelSettings;
  }

  public boolean isParentOneMainParent() {
    return isParentOneMainParent;
  }

  public void setParentOneMainParent(final boolean parentOneMainParent) {
    isParentOneMainParent = parentOneMainParent;
  }

  @Nonnull
  public String getCourseReportName() {
    return courseReportName;
  }

  public void setCourseReportName(@Nonnull final String courseReportName) {
    this.courseReportName = courseReportName;
  }

  public boolean isLimitCourseAssignmentsToActive() {
    return limitCourseAssignmentsToActive;
  }

  public void setLimitCourseAssignmentsToActive(final boolean limitCourseAssignmentsToActive) {
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }
}