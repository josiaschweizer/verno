package ch.verno.db.entity;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "title")
  private String title;

  @Column(name = "capacity")
  private Integer capacity;

  @Column(name = "location")
  private String location;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_level_id", nullable = false)
  private CourseLevelEntity level;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_schedule_id", nullable = false)
  private CourseScheduleEntity schedule;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
      name = "course_weekday",
      joinColumns = @JoinColumn(name = "course_id")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "weekday")
  private Set<DayOfWeek> weekdays;

  @Column(name = "duration")
  private Integer duration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "instructor")
  private InstructorEntity instructor;

  protected CourseEntity() {
    // JPA
  }

  public CourseEntity(@Nonnull final String title,
                      @Nullable final Integer capacity,
                      @Nonnull final String location,
                      @Nullable final CourseLevelEntity level,
                      @Nullable final CourseScheduleEntity schedule,
                      @Nonnull final Set<DayOfWeek> weekdays,
                      @Nullable final Integer duration,
                      @Nullable final InstructorEntity instructor) {
    this.title = title;
    this.capacity = capacity;
    this.location = location;
    this.level = level;
    this.schedule = schedule;
    this.weekdays = weekdays;
    this.duration = duration;
    this.instructor = instructor;
  }

  @Nonnull
  public static CourseEntity ref(@Nonnull final Long id) {
    final var entity = new CourseEntity();
    entity.setId(id);
    return entity;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(final Integer capacity) {
    this.capacity = capacity;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public CourseLevelEntity getLevel() {
    return level;
  }

  public void setLevel(final CourseLevelEntity level) {
    this.level = level;
  }

  public CourseScheduleEntity getSchedule() {
    return schedule;
  }

  public void setSchedule(final CourseScheduleEntity schedule) {
    this.schedule = schedule;
  }

  public Set<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public void setWeekdays(final Set<DayOfWeek> weekdays) {
    this.weekdays = weekdays;
  }

  public Integer getDuration() {
    return duration;
  }

  public void setDuration(final Integer duration) {
    this.duration = duration;
  }

  public InstructorEntity getInstructor() {
    return instructor;
  }

  public void setInstructor(final InstructorEntity instructor) {
    this.instructor = instructor;
  }
}