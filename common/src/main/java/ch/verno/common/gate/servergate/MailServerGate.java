package ch.verno.common.gate.servergate;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.lib.mail.placeholder.context.CourseMailPlaceholderContext;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public interface MailServerGate {

  void sendWelcomeMail(@Nonnull String to);

  void sendCourseEmails(@Nonnull MailContentDto mailContent,
                        @Nonnull List<PlaceholderValue<CourseMailPlaceholderContext>> placeHolderValues,
                        @Nullable List<ParticipantDto> participants,
                        @Nullable final CourseScheduleDto courseSchedule,
                        @Nullable final CourseDto course);
}
