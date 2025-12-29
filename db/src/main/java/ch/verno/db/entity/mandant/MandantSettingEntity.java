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

  protected MandantSettingEntity() {
    // JPA
  }

  public MandantSettingEntity(@Nonnull final Integer courseDaysPerSchedule,
                              @Nonnull final Integer maxParticipantsPerCourse) {
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
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
}
