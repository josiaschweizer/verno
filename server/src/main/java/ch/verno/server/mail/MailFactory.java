package ch.verno.server.mail;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.service.intern.mail.IMailConfigService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.placeholder.PlaceholderUtil;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.lib.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.common.server.mail.MailConfigOptions;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.simplejavamail.email.EmailBuilder;

import java.util.List;

public class MailFactory {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IMailConfigService mailConfigService;

  public MailFactory(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.mailConfigService = globalInterface.getService(IMailConfigService.class);
  }


  public void sendWelcomeMail(@Nonnull final String to) {
    final var mailConfig = mailConfigService.getConfigForCurrentTenant();
    final var fromEmail = mailConfig.getFromEmail();

    final var email = EmailBuilder.startingBlank()
            .from("Verno", fromEmail)
            .to(to)
            .withSubject("Willkommen bei Verno")
            .withHTMLText("""
                      <h2>Willkommen</h2>
                      <p>Deine Email Konfiguration ist bereit.</p>
                    """)
            .buildEmail();

    MailerUtil.sendMail(globalInterface, email);
  }


  public void sendCourseEmails(@Nonnull final MailContentDto mailContentDto,
                               @Nonnull final List<PlaceholderValue<CourseMailPlaceholderContext>> placeHolderValues,
                               @Nullable final List<ParticipantDto> participants,
                               @Nullable final CourseScheduleDto schedule,
                               @Nullable final CourseDto course) {
    if (participants == null) {
      return;
    }

    final var mailConfig = mailConfigService.getConfigForCurrentTenant();
    final var fromEmail = mailConfig.getFromEmail();

    for (final var participant : participants) {
      final var mailContext = new CourseMailPlaceholderContext(participant, schedule, course);

      final var subject = PlaceholderUtil.replacePlaceholders(
              mailContentDto.subject(),
              mailContext,
              placeHolderValues
      );
      final var content = PlaceholderUtil.replacePlaceholders(
              mailContentDto.content(),
              mailContext,
              placeHolderValues
      );

      final var to = participant.getEmail();
      sendMail(fromEmail, to, subject, content, MailConfigOptions.empty());
    }
  }

  public void sendMail(@Nonnull final String from,
                       @Nonnull final String to,
                       @Nonnull final String subject,
                       @Nonnull final String content,
                       @Nonnull final MailConfigOptions mailConfigOptions) {
    final var builder = EmailBuilder.startingBlank()
            .from(from)
            .to(to)
            .withSubject(subject);

    if (MailContentUtil.looksLikeHtml(content)) {
      builder.withHTMLText(content);
    } else {
      builder.withPlainText(content);
    }

    // we normally use the default mailer origin from tenant config -> but when coming from api we not always have this option (eg get in touch dialog from landing page)
    if (mailConfigOptions.mailOrigin() != null) {
      MailerUtil.sendMail(globalInterface, builder.buildEmail(), mailConfigOptions.mailOrigin());
    } else {
      MailerUtil.sendMail(globalInterface, builder.buildEmail());
    }
  }
}
