package ch.verno.db.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "course_schedule")
public class CourseScheduleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  private String title;

  @ElementCollection
  @CollectionTable(
          name = "course_schedule_week",
          joinColumns = @JoinColumn(name = "course_schedule_id")
  )
  @Column(name = "week", nullable = false)
  @OrderColumn(name = "sort_index")
  private List<String> weeks;

  protected CourseScheduleEntity() {
    // JPA
  }

  public CourseScheduleEntity(@Nonnull final String title,
                              @Nonnull final List<String> weeks) {
    this.title = title;
    this.weeks = weeks;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public List<String> getWeeks() {
    return weeks;
  }

  public void setWeeks(final List<String> weeks) {
    this.weeks = weeks;
  }
}