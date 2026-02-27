package ch.verno.server.mail;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderUtil;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import java.util.List;

public class MailUtil {

  @Nonnull private final Mailer mailer;

  public MailUtil(@Nonnull final GlobalInterface globalInterface) {
    mailer = MailFactory.createMailer(globalInterface);
  }


  public void sendWelcomeMail(String to) {
    final var email = EmailBuilder.startingBlank()
            .from("Verno", "noreply@deine-domain.ch")
            .to(to)
            .withSubject("Willkommen bei Verno")
            .withHTMLText("""
                      <h2>Willkommen</h2>
                      <p>Dein Account ist bereit.</p>
                    """)
            .buildEmail();

    mailer.sendMail(email);
  }

  public void sendCourseEmails(@Nonnull final MailContentDto mailContentDto,
                               @Nonnull final List<PlaceholderValue<ParticipantDto>> placeHolderValues,
                               @Nonnull final List<ParticipantDto> participants) {
    for (final var participant : participants) {
      final var subject = PlaceholderUtil.replacePlaceholders(
              mailContentDto.subject(),
              participant,
              placeHolderValues
      );
      final var content = PlaceholderUtil.replacePlaceholders(
              mailContentDto.content(),
              participant,
              placeHolderValues
      );

      final var builder = EmailBuilder.startingBlank()
              .from("Verno", "noreply@verno-app.ch")
              .to(participant.getEmail())
              .withSubject(subject);

      if (MailContentUtil.looksLikeHtml(content)) {
        builder.withHTMLText(content);
      } else {
        builder.withPlainText(content);
      }

      final var email = builder.buildEmail();
      mailer.sendMail(email);
    }
  }
}
