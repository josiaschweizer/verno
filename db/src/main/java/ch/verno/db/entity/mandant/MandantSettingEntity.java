package ch.verno.db.entity.mandant;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "mandant_settings")
public class MandantSettingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "course_weeks_per_schedule")
  private Integer courseDaysPerSchedule;

  @Column(name = "max_participants_per_course")
  private Integer maxParticipantsPerCourse;

  @Column(name = "enforce_quantity_settings")
  private boolean enforceQuantitySettings;

  @Column(name = "enforce_course_level_settings")
  private boolean enforceCourseLevelSettings;

  protected MandantSettingEntity() {
    // JPA
  }

  public MandantSettingEntity(@Nonnull final Integer courseDaysPerSchedule,
                              @Nonnull final Integer maxParticipantsPerCourse,
                              final boolean enforceQuantitySettings,
                              final boolean enforceCourseLevelSettings) {
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
    this.enforceQuantitySettings = enforceQuantitySettings;
    this.enforceCourseLevelSettings = enforceCourseLevelSettings;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
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
}
