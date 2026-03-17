package ch.verno.server.mail;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.MailServerGate;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.lib.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.lib.Lazy;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServerGateImpl implements MailServerGate {

  @Nonnull private final Lazy<MailFactory> mailHelper;

  public MailServerGateImpl(@Nonnull final GlobalInterface globalInterface) {
    this.mailHelper = Lazy.of(() -> new MailFactory(globalInterface));
  }

  @Override
  public void sendWelcomeMail(@Nonnull final String to) {
    mailHelper.get().sendWelcomeMail(to);
  }


  @Override
  public void sendCourseEmails(@Nonnull final MailContentDto mailContent,
                               @Nonnull final List<PlaceholderValue<CourseMailPlaceholderContext>> placeHolderValues,
                               @Nullable final List<ParticipantDto> participants,
                               @Nullable final CourseScheduleDto courseSchedule,
                               @Nullable final CourseDto course) {
    mailHelper.get().sendCourseEmails(
            mailContent,
            placeHolderValues,
            participants,
            courseSchedule,
            course);
  }

}
