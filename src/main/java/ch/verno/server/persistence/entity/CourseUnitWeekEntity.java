package ch.verno.server.persistence.entity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "course_unit_week",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_course_unit_week",
        columnNames = {"course_unit_id", "year", "calendar_week"}
    )
)
public class CourseUnitWeekEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_unit_id", nullable = false)
  private CourseUnitEntity courseUnit;

  @Column(name = "year", nullable = false)
  private int year;

  @Column(name = "calendar_week", nullable = false)
  private int calendarWeek;

  protected CourseUnitWeekEntity() {
  }

  public CourseUnitWeekEntity(final int year,
                              final int calendarWeek) {
    this.year = year;
    this.calendarWeek = calendarWeek;
  }

  public Long getId() {
    return id;
  }

  public int getYear() {
    return year;
  }

  public int getCalendarWeek() {
    return calendarWeek;
  }

  public @Nonnull CourseUnitEntity getCourseUnit() {
    return courseUnit;
  }

  void setCourseUnit(@Nullable final CourseUnitEntity courseUnit) {
    this.courseUnit = courseUnit;
  }
}