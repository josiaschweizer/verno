package ch.verno.db.entity.mandant;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "mandant_settings")
public class MandantSettingEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private Long id;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id", nullable = false)
  private MandantEntity mandant;

  @Column(name = "course_weeks_per_schedule")
  private Integer courseDaysPerSchedule;

  @Column(name = "max_participants_per_course")
  private Integer maxParticipantsPerCourse;

  @Column(name = "enforce_quantity_settings")
  private boolean enforceQuantitySettings;

  @Column(name = "enforce_course_level_settings")
  private boolean enforceCourseLevelSettings;

  @Column(name = "is_parent_one_main_parent")
  private boolean isParentOneMainParent;

  @Column(name = "course_report_name")
  private String courseReportName;

  @Column(name = "limit_course_assignments_to_active")
  private boolean limitCourseAssignmentsToActive;

  protected MandantSettingEntity() {
    // JPA
  }

  public MandantSettingEntity(@Nonnull final MandantEntity mandant,
                              @Nonnull final Integer courseDaysPerSchedule,
                              @Nonnull final Integer maxParticipantsPerCourse,
                              final boolean enforceQuantitySettings,
                              final boolean enforceCourseLevelSettings,
                              final boolean isParentOneMainParent,
                              @Nonnull final String courseReportName,
                              final boolean limitCourseAssignmentsToActive) {
    this.mandant = mandant;
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
    this.enforceQuantitySettings = enforceQuantitySettings;
    this.enforceCourseLevelSettings = enforceCourseLevelSettings;
    this.isParentOneMainParent = isParentOneMainParent;
    this.courseReportName = courseReportName;
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
  }

  public Integer getCourseDaysPerSchedule() {
    return courseDaysPerSchedule;
  }

  public void setCourseDaysPerSchedule(final Integer quantityOfCoursesPerSchedule) {
    this.courseDaysPerSchedule = quantityOfCoursesPerSchedule;
  }

  public Integer getMaxParticipantsPerCourse() {
    return maxParticipantsPerCourse;
  }

  public void setMaxParticipantsPerCourse(final Integer maxParticipantsPerCourse) {
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

  public String getCourseReportName() {
    return courseReportName;
  }

  public void setCourseReportName(final String courseReportName) {
    this.courseReportName = courseReportName;
  }

  public boolean isLimitCourseAssignmentsToActive() {
    return limitCourseAssignmentsToActive;
  }

  public void setLimitCourseAssignmentsToActive(final boolean limitCourseAssignmentsToActive) {
    this.limitCourseAssignmentsToActive = limitCourseAssignmentsToActive;
  }
}