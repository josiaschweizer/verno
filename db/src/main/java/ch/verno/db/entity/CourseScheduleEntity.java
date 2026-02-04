package ch.verno.db.entity;

import ch.verno.common.base.components.colorpicker.Colors;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.mandant.MandantEntityListener;
import ch.verno.db.entity.mandant.MandantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;

@Entity
@Table(name = "course_schedule", schema = "public")
@EntityListeners(MandantEntityListener.class)
public class CourseScheduleEntity extends MandantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "color", nullable = false, length = 16)
  private String color = Colors.PRIMARY_COLOR;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 16)
  private CourseScheduleStatus status = CourseScheduleStatus.PLANNED;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
          name = "course_schedule_week",
          joinColumns = @JoinColumn(name = "course_schedule_id")
  )
  @OrderColumn(name = "sort_index")
  @Column(name = "week")
  private List<String> weeks;

  protected CourseScheduleEntity() {
    // JPA
  }

  public CourseScheduleEntity(@Nonnull final MandantEntity mandant,
                              @Nonnull final String title,
                              @Nonnull final String color,
                              @Nonnull final CourseScheduleStatus status,
                              @Nonnull final List<String> weeks) {
    setMandant(mandant);
    this.title = title;
    this.color = color;
    this.status = status;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getColor() {
    return color;
  }

  public void setColor(final String color) {
    this.color = color;
  }

  public CourseScheduleStatus getStatus() {
    return status;
  }

  public void setStatus(final CourseScheduleStatus status) {
    this.status = status;
  }

  public List<String> getWeeks() {
    return weeks;
  }

  public void setWeeks(final List<String> weeks) {
    this.weeks = weeks;
  }
}