package ch.verno.contract.dto.table.setting;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class TenantSettingDto extends BaseDto {

  @Nonnull private Integer courseDaysPerSchedule;
  @Nonnull private Integer maxParticipantsPerCourse;
  private boolean enforceQuantitySettings;
  private boolean enforceCourseLevelSettings;
  private boolean isParentOneMainParent;
  @Nonnull private String courseReportName;
  @Nullable private Long courseReportTemplate;
  private boolean limitCourseAssignmentsToActive;

  private TenantSettingDto() {
    setId(null);
    this.courseDaysPerSchedule = Publ.ZERO;
    this.maxParticipantsPerCourse = Publ.ZERO;
    this.enforceQuantitySettings = false;
    this.enforceCourseLevelSettings = false;
    this.isParentOneMainParent = false;
    this.courseReportName = Publ.EMPTY_STRING;
    this.courseReportTemplate = null;
    this.limitCourseAssignmentsToActive = false;
  }

  public TenantSettingDto(@Nonnull final Integer courseDaysPerSchedule,
                          @Nonnull final Integer maxParticipantsPerCourse,
                          final boolean enforceQuantitySettings,
                          final boolean enforceCourseLevelSettings,
                          final boolean isParentOneMainParent,
                          @Nonnull final String courseReportName,
                          @Nullable final Long courseReportTemplate,
                          final boolean limitCourseAssignmentsToActive) {
    this(null, courseDaysPerSchedule, maxParticipantsPerCourse, enforceQuantitySettings, enforceCourseLevelSettings, isParentOneMainParent, courseReportName, courseReportTemplate, limitCourseAssignmentsToActive);
  }

  public TenantSettingDto(@Nullable final Long id,
                          @Nonnull final Integer courseDaysPerSchedule,
                          @Nonnull final Integer maxParticipantsPerCourse,
                          final boolean enforceQuantitySettings,
                          final boolean enforceCourseLevelSettings,
                          final boolean isParentOneMainParent,
                          @Nonnull final String courseReportName,
                          @Nullable final Long courseReportTemplate,
                          final boolean limitCourseAssignmentsToActive) {
    setId(id);
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
    this.enforceQuantitySettings = enforceQuantitySettings;
    this.enforceCourseLevelSettings = enforceCourseLevelSettings;
    this.isParentOneMainParent = isParentOneMainParent;
    this.courseReportName = courseReportName;
    this.courseReportTemplate = courseReportTemplate;
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }

  @Nonnull
  public static TenantSettingDto empty() {
    return new TenantSettingDto();
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

  @Nullable
  public Long getCourseReportTemplate() {
    return courseReportTemplate;
  }

  public void setCourseReportTemplate(@Nullable final Long courseReportTemplate) {
    this.courseReportTemplate = courseReportTemplate;
  }

  public boolean isLimitCourseAssignmentsToActive() {
    return limitCourseAssignmentsToActive;
  }

  public void setLimitCourseAssignmentsToActive(final boolean limitCourseAssignmentsToActive) {
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }
}