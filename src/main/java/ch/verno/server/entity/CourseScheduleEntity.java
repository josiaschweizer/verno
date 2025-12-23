package ch.verno.server.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "course_schedule")
public class CourseScheduleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "week_start", nullable = false)
  private Integer weekStart;

  @Column(name = "week_end", nullable = false)
  private Integer weekEnd;

  protected CourseScheduleEntity() {
  }

  public Long getId() {
    return id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public Integer getWeekStart() {
    return weekStart;
  }

  public void setWeekStart(final Integer weekStart) {
    this.weekStart = weekStart;
  }

  public Integer getWeekEnd() {
    return weekEnd;
  }

  public void setWeekEnd(final Integer weekEnd) {
    this.weekEnd = weekEnd;
  }
}