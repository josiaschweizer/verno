package ch.verno.db.entity;

import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class CourseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "mandant_id", nullable = false)
  private MandantEntity mandant;

  @Nonnull
  @Column(name = "created_at", nullable = false, updatable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Nonnull
  @Column(name = "title", nullable = false)
  private String title;

  @Nullable
  @Column(name = "capacity")
  private Integer capacity;

  @Nonnull
  @Column(name = "location", nullable = false)
  private String location;

  @Nonnull
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "course_course_level",
          joinColumns = @JoinColumn(name = "course_id"),
          inverseJoinColumns = @JoinColumn(name = "course_level_id")
  )
  @OrderColumn(name = "sort_index")
  private List<CourseLevelEntity> courseLevels = new ArrayList<>();

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "course_schedule_id", nullable = false)
  private CourseScheduleEntity courseSchedule;

  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(
          name = "course_weekday",
          joinColumns = @JoinColumn(name = "course_id")
  )
  @Enumerated(EnumType.STRING)
  @Column(name = "weekday", nullable = false)
  @OrderColumn(name = "sort_index")
  private List<DayOfWeek> weekdays = new ArrayList<>();

  @Nullable
  @Column(name = "start_time")
  private LocalTime startTime;

  @Nullable
  @Column(name = "end_time")
  private LocalTime endTime;

  @Nullable
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "instructor_id")
  private InstructorEntity instructor;

  @Nonnull
  @Column(name = "note", columnDefinition = "TEXT")
  private String note;

  protected CourseEntity() {
  }

  public CourseEntity(@Nonnull final MandantEntity mandant,
                      @Nonnull final String title,
                      @Nullable final Integer capacity,
                      @Nonnull final String location,
                      @Nullable final List<CourseLevelEntity> courseLevels,
                      @Nullable final CourseScheduleEntity courseSchedule,
                      @Nonnull final List<DayOfWeek> weekdays,
                      @Nullable final LocalTime startTime,
                      @Nullable final LocalTime endTime,
                      @Nullable final InstructorEntity instructor,
                      @Nonnull final String note) {
    this.mandant = mandant;
    this.title = title;
    this.capacity = capacity;
    this.location = location;
    this.courseLevels = courseLevels != null ? courseLevels : new ArrayList<>();
    this.courseSchedule = courseSchedule;
    this.weekdays = weekdays;
    this.startTime = startTime;
    this.endTime = endTime;
    this.instructor = instructor;
    this.note = note;
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

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
  }

  @Nonnull
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  public void setTitle(@Nonnull final String title) {
    this.title = title;
  }

  @Nullable
  public Integer getCapacity() {
    return capacity;
  }

  public void setCapacity(@Nullable final Integer capacity) {
    this.capacity = capacity;
  }

  @Nonnull
  public String getLocation() {
    return location;
  }

  public void setLocation(@Nonnull final String location) {
    this.location = location;
  }

  @Nonnull
  public List<CourseLevelEntity> getCourseLevels() {
    return courseLevels;
  }

  public void setCourseLevels(@Nonnull final List<CourseLevelEntity> courseLevels) {
    this.courseLevels.clear();
    this.courseLevels.addAll(courseLevels);
  }

  @Nullable
  public CourseScheduleEntity getCourseSchedule() {
    return courseSchedule;
  }

  public void setCourseSchedule(@Nullable final CourseScheduleEntity schedule) {
    this.courseSchedule = schedule;
  }

  @Nonnull
  public List<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public void setWeekdays(@Nonnull final List<DayOfWeek> weekdays) {
    this.weekdays.clear();
    this.weekdays.addAll(weekdays);
  }

  @Nullable
  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(@Nullable final LocalTime startTime) {
    this.startTime = startTime;
  }

  @Nullable
  public LocalTime getEndTime() {
    return endTime;
  }

  public void setEndTime(@Nullable final LocalTime endTime) {
    this.endTime = endTime;
  }

  @Nullable
  public InstructorEntity getInstructor() {
    return instructor;
  }

  public void setInstructor(@Nullable final InstructorEntity instructor) {
    this.instructor = instructor;
  }

  @Nonnull
  public String getNote() {
    return note;
  }

  public void setNote(@Nonnull final String note) {
    this.note = note;
  }
}