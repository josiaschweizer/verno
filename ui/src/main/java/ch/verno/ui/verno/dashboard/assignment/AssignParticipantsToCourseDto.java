package ch.verno.ui.verno.dashboard.assignment;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

import java.util.Set;

public class AssignParticipantsToCourseDto {

  @Nonnull
  private CourseDto course;

  @Nonnull
  private Set<ParticipantDto> participants;

  public AssignParticipantsToCourseDto() {
    course = CourseDto.empty();
    participants = Set.of();
  }

  public AssignParticipantsToCourseDto(@Nonnull final CourseDto course,
                                       @Nonnull final Set<ParticipantDto> participants) {
    this.course = course;
    this.participants = participants;
  }

  @Nonnull
  public CourseDto getCourse() {
    return course;
  }

  public void setCourse(@Nonnull final CourseDto course) {
    this.course = course;
  }

  @Nonnull
  public Set<ParticipantDto> getParticipants() {
    return participants;
  }

  public void setParticipants(@Nonnull final Set<ParticipantDto> participants) {
    this.participants = participants;
  }
}