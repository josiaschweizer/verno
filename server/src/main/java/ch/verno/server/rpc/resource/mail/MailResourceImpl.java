package ch.verno.server.rpc.resource.mail;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.mail.MailResource;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.placeholder.PlaceholderValue;
import ch.verno.contract.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.mail.MailSenderService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class MailResourceImpl implements MailResource {

  @Nonnull private final Lazy<MailSenderService> mailSenderService;

  public MailResourceImpl(@Nonnull final ServerBean serverBean) {
    this.mailSenderService = Lazy.of(() -> serverBean.get(MailSenderService.class));
  }

  @Override
  public void sendWelcomeMail(@Nonnull final String recipient) {
    mailSenderService.get().sendWelcomeMail(recipient);
  }

  @Override
  public void sendCourseMails(@Nonnull final MailContentDto mailContent,
                              @Nonnull final List<PlaceholderValue<CourseMailPlaceholderContext>> placeholderValues,
                              @Nullable final List<ParticipantDto> participants,
                              @Nullable final CourseScheduleDto schedule,
                              @Nullable final CourseDto course) {
    mailSenderService.get().sendCourseMails(mailContent, placeholderValues, participants, schedule, course);
  }
}
