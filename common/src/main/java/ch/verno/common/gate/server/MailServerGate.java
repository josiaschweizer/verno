package ch.verno.common.gate.server;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.lib.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.common.server.mail.MailConfigOptions;
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

  void sendMail(@Nonnull String from,
                @Nonnull String to,
                @Nonnull String subject,
                @Nonnull String content,
                @Nonnull MailConfigOptions mailConfigOptions);
}
