package ch.verno.db.entity.mandant;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

@Entity
@Table(name = "mandant_settings")
public class MandantSettingEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quantity_of_courses_per_schedule")
  private Integer quantityOfCoursesPerSchedule;

  protected MandantSettingEntity() {
    // JPA
  }

  public MandantSettingEntity(@Nonnull final Integer quantityOfCoursesPerSchedule) {
    this.quantityOfCoursesPerSchedule = quantityOfCoursesPerSchedule;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public Integer getQuantityOfCoursesPerSchedule() {
    return quantityOfCoursesPerSchedule;
  }

  public void setQuantityOfCoursesPerSchedule(final Integer quantityOfCoursesPerSchedule) {
    this.quantityOfCoursesPerSchedule = quantityOfCoursesPerSchedule;
  }
}
