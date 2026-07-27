package ch.verno.contract.dto.table.course;

import ch.verno.common.dto.ui.colorpicker.Colors;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.lang.ObjectUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CourseDto extends BaseDto<Long> {

  @Nonnull private String title;
  @Nullable private Integer capacity;
  @Nonnull private String location;

  @Nonnull private List<CourseLevelDto> courseLevels;
  @Nullable private CourseScheduleDto courseSchedule;

  @Nonnull private List<DayOfWeek> weekdays;
  @Nullable private LocalTime startTime;
  @Nullable private LocalTime endTime;

  @Nullable private InstructorDto instructor;
  @Nonnull private List<InstructorDto> secondaryInstructors;

  @Nonnull private String note;
  @Nonnull private String color;

  private CourseDto() {
    setId(null);
    this.title = Publ.EMPTY_STRING;
    this.capacity = 0;
    this.location = Publ.EMPTY_STRING;
    this.courseLevels = New.list();
    this.courseSchedule = null;
    this.weekdays = New.list();
    this.startTime = null;
    this.endTime = null;
    this.instructor = null;
    this.secondaryInstructors = New.list();
    this.note = Publ.EMPTY_STRING;
    this.color = Publ.EMPTY_STRING;
  }

  public CourseDto(@Nonnull final String title,
                   @Nullable final Integer capacity,
                   @Nonnull final String location,
                   @Nonnull final List<CourseLevelDto> courseLevels,
                   @Nonnull final CourseScheduleDto courseSchedule,
                   @Nonnull final List<DayOfWeek> weekdays,
                   @Nullable final LocalTime startTime,
                   @Nullable final LocalTime endTime,
                   @Nullable final InstructorDto instructor,
                   @Nonnull final List<InstructorDto> secondaryInstructors,
                   @Nonnull final String note,
                   @Nonnull final String color) {
    this(null,
            title,
            capacity,
            location,
            courseLevels,
            courseSchedule,
            weekdays,
            startTime,
            endTime,
            instructor,
            secondaryInstructors,
            note,
            color
    );
  }

  public CourseDto(@Nullable final Long id,
                   @Nonnull final String title,
                   @Nullable final Integer capacity,
                   @Nonnull final String location,
                   @Nonnull final List<CourseLevelDto> courseLevels,
                   @Nonnull final CourseScheduleDto courseSchedule,
                   @Nonnull final List<DayOfWeek> weekdays,
                   @Nullable final LocalTime startTime,
                   @Nullable final LocalTime endTime,
                   @Nullable final InstructorDto instructor,
                   @Nonnull final List<InstructorDto> secondaryInstructors,
                   @Nonnull final String note,
                   @Nonnull final String color) {
    super.setId(id);
    this.title = title;
    this.capacity = capacity;
    this.location = location;
    this.courseLevels = courseLevels;
    this.courseSchedule = courseSchedule;
    this.weekdays = weekdays;
    this.startTime = startTime;
    this.endTime = endTime;
    this.instructor = instructor;
    this.secondaryInstructors = secondaryInstructors;
    this.note = note;
    this.color = color;
  }

  @Nonnull
  public static CourseDto empty() {
    return new CourseDto();
  }

  @Nonnull
  public static CourseDto ref(@Nonnull final Long id) {
    final var dto = CourseDto.empty();
    dto.setId(id);
    return dto;
  }

  public boolean isEmpty() {
    return ObjectUtil.isEmpty(getId()) &&
            ObjectUtil.isEmpty(title) &&
            ObjectUtil.isEmpty(capacity) &&
            ObjectUtil.isEmpty(location) &&
            ObjectUtil.isEmpty(courseLevels) &&
            ObjectUtil.isEmpty(courseSchedule) &&
            ObjectUtil.isEmpty(weekdays) &&
            ObjectUtil.isEmpty(startTime) &&
            ObjectUtil.isEmpty(endTime) &&
            ObjectUtil.isEmpty(instructor) &&
            ObjectUtil.isEmpty(secondaryInstructors) &&
            ObjectUtil.isEmpty(note) &&
            ObjectUtil.isEmpty(color);
  }

  @Nonnull
  public String getTitle() {
    return title;
  }

  public void setTitle(@Nonnull final String title) {
    this.title = title;
  }

  @Nonnull
  public String displayName() {
    return title;
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
  public List<CourseLevelDto> getCourseLevels() {
    return courseLevels;
  }

  public void setCourseLevels(@Nonnull final List<CourseLevelDto> courseLevels) {
    this.courseLevels = courseLevels;
  }

  @Nonnull
  public String getCourseLevelAsString() {
    if (courseLevels.isEmpty()) {
      return Publ.EMPTY_STRING;
    }

    final var stringBuilder = new StringBuilder();
    for (final CourseLevelDto level : courseLevels) {
      if (level == null) {
        continue;
      }
      if (!stringBuilder.isEmpty()) {
        stringBuilder.append(Publ.COMMA).append(Publ.SPACE);
      }

      stringBuilder.append(level.displayName());
    }

    return stringBuilder.toString();
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
    if (courseSchedule == null || courseSchedule.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return courseSchedule.displayName();
  }

  @Nonnull
  public String getCourseScheduleStatusKey() {
    if (courseSchedule == null || courseSchedule.isEmpty()) {
      return Publ.EMPTY_STRING;
    }
    return courseSchedule.getStatus().getDisplayNameKey();
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
      if (day == null) {
        continue;
      }
      if (!stringBuilder.isEmpty()) {
        stringBuilder.append(Publ.COMMA).append(Publ.SPACE);
      }
      stringBuilder.append(day.name(), 0, 3);
    }

    return stringBuilder.toString();
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
  public List<InstructorDto> getSecondaryInstructors() {
    return secondaryInstructors;
  }

  public void setSecondaryInstructors(@Nonnull final List<InstructorDto> secondaryInstructors) {
    this.secondaryInstructors = secondaryInstructors;
  }

  @Nonnull
  public String getNote() {
    return note;
  }

  public void setNote(@Nonnull final String note) {
    this.note = note;
  }

  @Nonnull
  public String getColor() {
    return color;
  }

  public void setColor(@Nullable final String color) {
    this.color = Optional.ofNullable(color).orElse(Colors.PRIMARY_COLOR);
  }

  @Override
  public boolean equals(@Nullable final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CourseDto other)) {
      return false;
    }

    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}