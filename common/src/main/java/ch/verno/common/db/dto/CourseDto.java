package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.util.List;

public class CourseDto extends BaseDto {

  @Nonnull
  private String title;

  @Nullable
  private Integer capacity;

  @Nonnull
  private String location;

  @Nullable
  private CourseLevelDto courseLevel;

  @Nullable
  private CourseScheduleDto courseSchedule;

  @Nonnull
  private List<DayOfWeek> weekdays;

  @Nullable
  private Integer duration;

  @Nullable
  private InstructorDto instructor;

  public CourseDto() {
    this(null,
            Publ.EMPTY_STRING,
            null,
            Publ.EMPTY_STRING,
            CourseLevelDto.empty(),
            CourseScheduleDto.empty(),
            List.of(),
            null,
            null
    );
  }

  public CourseDto(@Nullable final Long id,
                   @Nonnull final String title,
                   @Nullable final Integer capacity,
                   @Nonnull final String location,
                   @Nonnull final CourseLevelDto courseLevel,
                   @Nonnull final CourseScheduleDto courseSchedule,
                   @Nonnull final List<DayOfWeek> weekdays,
                   @Nullable final Integer duration,
                   @Nullable final InstructorDto instructor) {
    super.setId(id);
    this.title = title;
    this.capacity = capacity;
    this.location = location;
    this.courseLevel = courseLevel;
    this.courseSchedule = courseSchedule;
    this.weekdays = weekdays;
    this.duration = duration;
    this.instructor = instructor;
  }

  public static CourseDto empty() {
    return new CourseDto();
  }

  public boolean isEmpty() {
    return this.getId() != null
            && this.getId() == 0L
            && this.title.isEmpty()
            && (this.capacity == null || this.capacity == 0)
            && this.location.isEmpty()
            && this.courseLevel.isEmpty()
            && this.courseSchedule.isEmpty()
            && this.weekdays.isEmpty()
            && (this.duration == null || this.duration == 0)
            && (this.instructor == null || this.instructor.isEmpty());
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

  @Nullable
  public CourseLevelDto getCourseLevel() {
    return courseLevel;
  }

  public void setCourseLevel(@Nullable final CourseLevelDto courseLevel) {
    this.courseLevel = courseLevel;
  }

  @Nonnull
  public String getCourseLevelAsString() {
    if (courseLevel == null || courseLevel.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return courseLevel.displayName();
  }

  @Nullable
  public CourseScheduleDto getCourseSchedule() {
    return courseSchedule;
  }

  public void setCourseSchedule(@Nullable final CourseScheduleDto courseSchedule) {
    this.courseSchedule = courseSchedule;
  }

  @Nonnull
  public String getCourseScheduleAsString() {
    if (courseSchedule.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return courseSchedule.getDisplayTitle();
  }

  @Nonnull
  public List<DayOfWeek> getWeekdays() {
    return weekdays;
  }

  public void setWeekdays(@Nonnull final List<DayOfWeek> weekdays) {
    this.weekdays = weekdays;
  }

  @Nonnull
  public String getWeekdaysAsString() {
    if (weekdays.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var stringBuilder = new StringBuilder();
    for (final DayOfWeek day : weekdays) {
      stringBuilder.append(day.name(), 0, 3).append(Publ.COMMA + Publ.SPACE);
    }

    stringBuilder.setLength(stringBuilder.length() - 2);
    return stringBuilder.toString();
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

  @Nonnull
  public String getInstructorAsString() {
    if (instructor == null || instructor.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return instructor.displayName();
  }

  @Nonnull
  public String displayName() {
    return title;
  }
}