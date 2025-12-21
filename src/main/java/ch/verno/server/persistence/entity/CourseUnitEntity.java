package ch.verno.server.persistence.entity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "course_unit")
public class CourseUnitEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * FK in course_unit.course_id (muss in DB UNIQUE sein für echtes 1:1)
   */
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_id", nullable = false, unique = true)
  private CourseEntity course;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @OneToMany(mappedBy = "courseUnit", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<CourseUnitWeekEntity> weeks = new ArrayList<>();

  protected CourseUnitEntity() {
  }

  @Nonnull
  public static CourseUnitEntity of(@Nonnull final String name,
                                    @Nonnull final Instant createdAt) {
    final var entity = new CourseUnitEntity();
    entity.setName(name);
    entity.setCreatedAt(createdAt);
    return entity;
  }

  @Nonnull
  public static CourseUnitEntity create(
      @Nonnull final CourseEntity course,
      @Nonnull final String name,
      @Nonnull final Instant createdAt
  ) {
    final var e = new CourseUnitEntity();
    e.course = Objects.requireNonNull(course, "course");
    e.name = Objects.requireNonNull(name, "name");
    e.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    return e;
  }

  @Nullable
  public Long getId() {
    return id;
  }

  @Nonnull
  public CourseEntity getCourse() {
    return course;
  }

  public void setCourse(@Nonnull final CourseEntity course) {
    this.course = Objects.requireNonNull(course, "course");
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public void setName(@Nonnull final String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  @Nonnull
  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nonnull final Instant createdAt) {
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
  }

  @Nonnull
  public List<CourseUnitWeekEntity> getWeeks() {
    return weeks;
  }

  public void addWeek(@Nonnull final CourseUnitWeekEntity week) {
    Objects.requireNonNull(week, "week");
    weeks.add(week);
    week.setCourseUnit(this);
  }

  public void removeWeek(@Nonnull final CourseUnitWeekEntity week) {
    Objects.requireNonNull(week, "week");
    if (weeks.remove(week)) {
      week.setCourseUnit(null);
    }
  }

  public void clearWeeks() {
    for (final var week : weeks) {
      week.setCourseUnit(null);
    }
    weeks.clear();
  }
}