package ch.verno.server.persistence.entity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "title")
  private String title;

  @Column(name = "capacity")
  private Integer capacity;

  @Column(name = "location")
  private String location;

  @Column(name = "duration")
  private Integer durationMinutes;

  @Column(name = "course_level")
  private Long courseLevelId;

  @Column(name = "instructor")
  private Long instructorId;

  @OneToOne(
      mappedBy = "course",
      cascade = CascadeType.ALL,
      orphanRemoval = true
  )
  private CourseUnitEntity courseUnit;

  protected CourseEntity() {
  }

  @Nonnull
  public static CourseEntity create(@Nonnull final Instant createdAt) {
    final var entity = new CourseEntity();
    entity.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    return entity;
  }

  public Long getId() {
    return id;
  }

  @Nonnull
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Nullable
  public String getTitle() {
    return title;
  }

  @Nullable
  public Integer getCapacity() {
    return capacity;
  }

  @Nullable
  public String getLocation() {
    return location;
  }

  @Nullable
  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  @Nullable
  public Long getCourseLevelId() {
    return courseLevelId;
  }

  @Nullable
  public Long getInstructorId() {
    return instructorId;
  }

  @Nullable
  public CourseUnitEntity getCourseUnit() {
    return courseUnit;
  }

  public void setCreatedAt(@Nonnull final Instant createdAt) {
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
  }

  public void setTitle(@Nullable final String title) {
    this.title = title;
  }

  public void setCapacity(@Nullable final Integer capacity) {
    this.capacity = capacity;
  }

  public void setLocation(@Nullable final String location) {
    this.location = location;
  }

  public void setDurationMinutes(@Nullable final Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public void setCourseLevelId(@Nullable final Long courseLevelId) {
    this.courseLevelId = courseLevelId;
  }

  public void setInstructorId(@Nullable final Long instructorId) {
    this.instructorId = instructorId;
  }

  public void setCourseUnit(@Nullable final CourseUnitEntity courseUnit) {
    this.courseUnit = courseUnit;
    if (courseUnit != null) {
      courseUnit.setCourse(this);
    }
  }
}