package ch.verno.server.service.mail;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.mail.MailConfigOptions;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailDto;
import ch.verno.contract.mail.placeholder.PlaceholderUtil;
import ch.verno.contract.mail.placeholder.PlaceholderValue;
import ch.verno.contract.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.lib.Lazy;
import ch.verno.lib.VernoConstants;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mail.MailComposer;
import ch.verno.server.mail.mailer.MailSender;
import ch.verno.server.service.entity.mail.MailConfigService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailSenderService {

  @Nonnull private final Lazy<MailSender> mailSender;
  @Nonnull private final Lazy<MailConfigService> mailConfigService;

  public MailSenderService(@Nonnull final ServerBean serverBean) {
    this.mailSender = Lazy.of(() -> serverBean.get(MailSender.class));
    this.mailConfigService = Lazy.of(() -> serverBean.get(MailConfigService.class));
  }

  public void sendWelcomeMail(@Nonnull final String recipient) {
    final var mailDto = createWelcomeMail(recipient);
    final var email = MailComposer.createMail(mailDto);

    mailSender.get().send(email, MailConfigOptions.MailOrigin.TENANT_CONFIG);
  }

  @Nonnull
  private MailDto createWelcomeMail(@Nonnull final String recipient) {
    final var from = getTenantFromEmail();
    final var content = new MailContentDto( //TODO TRANSLATION!!!!
            "Willkommen bei Verno",
            """
                    <h2>Willkommen</h2>
                    <p>Deine E-Mail-Konfiguration ist bereit.</p>
                    """,
            null
    );

    return new MailDto(
            VernoConstants.VERNO,
            from,
            recipient,
            content,
            new MailConfigOptions(MailConfigOptions.MailOrigin.TENANT_CONFIG)
    );
  }

  public void sendCourseMails(@Nonnull final MailContentDto mailContent,
                              @Nonnull final List<PlaceholderValue<CourseMailPlaceholderContext>> placeholderValues,
                              @Nullable final List<ParticipantDto> participants,
                              @Nullable final CourseScheduleDto schedule,
                              @Nullable final CourseDto course) {
    if (participants == null || participants.isEmpty()) {
      return;
    }

    final var from = getTenantFromEmail();
    for (final var participant : participants) {
      if (participant.getEmail().isBlank()) {
        continue;
      }

      final var context = new CourseMailPlaceholderContext(participant, schedule, course);
      final var subject = PlaceholderUtil.replacePlaceholders(
              mailContent.subject(),
              context,
              placeholderValues
      );
      final var content = PlaceholderUtil.replacePlaceholders(
              mailContent.content(),
              context,
              placeholderValues
      );

      sendMail(MailDto.simple(
              from,
              participant.getEmail(),
              subject,
              content
      ));
    }
  }

  public void sendMail(@Nonnull final MailDto mailDto) {
    final var email = MailComposer.createMail(mailDto);

    final var origin = mailDto.options().mailOrigin() != null
            ? mailDto.options().mailOrigin()
            : MailConfigOptions.MailOrigin.TENANT_CONFIG;
    mailSender.get().send(email, origin);
  }

  @Nonnull
  private String getTenantFromEmail() {
    return mailConfigService.get().getRequiredMailConfigForCurrentTenant().getFromEmail();
  }

}