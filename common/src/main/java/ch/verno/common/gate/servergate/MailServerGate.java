package ch.verno.common.gate.servergate;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface MailServerGate {

  void sendWelcomeMail(@Nonnull String to);

  void sendCourseEmails(@Nonnull MailContentDto mailContent,
                        @Nonnull List<PlaceholderValue<ParticipantDto>> placeHolderValues,
                        @Nonnull List<ParticipantDto> participants);
}
