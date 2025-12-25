package ch.verno.common.db.dto;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.Set;

public class CourseDto {

  @Nullable
  private Long id;

  @Nonnull
  private String title;

  @Nullable
  private Integer capacity;

  @Nonnull
  private String location;

  @Nonnull
  private CourseLevelDto level;

  @Nonnull
  private CourseScheduleDto schedule;

  @Nonnull
  private Set<DayOfWeek> weekdays;

  @Nullable
  private Integer duration;

  @Nullable
  private InstructorDto instructor;

  public CourseDto() {
    this(
            0L,
            Publ.EMPTY_STRING,
            null,
            Publ.EMPTY_STRING,
            CourseLevelDto.empty(),
            CourseScheduleDto.empty(),
            Set.of(),
            null,
            null
    );
  }

  public CourseDto(@Nullable final Long id,
                   @Nonnull final String title,
                   @Nullable final Integer capacity,
                   @Nonnull final String location,
                   @Nonnull final CourseLevelDto level,
                   @Nonnull final CourseScheduleDto schedule,
                   @Nonnull final Set<DayOfWeek> weekdays,
                   @Nullable final Integer duration,
                   @Nullable final InstructorDto instructor) {
    this.id = id;
    this.title = title;
    this.capacity = capacity;
    this.location = location;
    this.level = level;
    this.schedule = schedule;
    this.weekdays = weekdays;
    this.duration = duration;
    this.instructor = instructor;
  }

  public static CourseDto empty() {
    return new CourseDto();
  }

  public boolean isEmpty() {
    return this.id != null
            && this.id == 0L
            && this.title.isEmpty()
            && (this.capacity == null || this.capacity == 0)
            && this.location.isEmpty()
            && this.level.isEmpty()
            && this.schedule.isEmpty()
            && this.weekdays.isEmpty()
            && (this.duration == null || this.duration == 0)
            && (this.instructor == null || this.instructor.isEmpty());
  }

  @Nonnull
  public String displayName() {
    return title;
  }

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
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
  public CourseLevelDto getLevel() {
    return level;
  }

  public void setLevel(@Nonnull final CourseLevelDto level) {
    this.level = level;
  }

  @Nonnull
  public CourseScheduleDto getSchedule() {
    return schedule;
  }

  public void setSchedule(@Nonnull final CourseScheduleDto schedule) {
    this.schedule = schedule;
  }

  @Nonnull
  public Set<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public void setWeekdays(@Nonnull final Set<DayOfWeek> weekdays) {
    this.weekdays = weekdays;
  }

  @Nullable
  public Integer getDuration() {
    return duration;
  }

  public void setDuration(@Nullable final Integer duration) {
    this.duration = duration;
  }

  @Nullable
  public InstructorDto getInstructor() {
    return instructor;
  }

  public void setInstructor(@Nullable final InstructorDto instructor) {
    this.instructor = instructor;
  }
}