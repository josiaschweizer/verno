package ch.verno.common.lib.mail.placeholder.context;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseMailPlaceholderContext(@Nonnull ParticipantDto participant,
                                           @Nullable CourseScheduleDto courseSchedule,
                                           @Nullable CourseDto course) implements MailContext {

  @Override
  public ParticipantDto participant() {
    return participant;
  }

  @Override
  public CourseScheduleDto courseSchedule() {
    return courseSchedule;
  }

  @Override
  public CourseDto course() {
    return course;
  }
}