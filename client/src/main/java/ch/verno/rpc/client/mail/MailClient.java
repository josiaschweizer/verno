package ch.verno.rpc.client.mail;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.mail.MailResource;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.placeholder.PlaceholderValue;
import ch.verno.contract.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class MailClient {

  @Nonnull private final Lazy<MailResource> mailResource;

  public MailClient(@Nonnull final RpcFactory rpcFactory) {
    this.mailResource = Lazy.of(() -> rpcFactory.create(MailResource.class));
  }

  public void sendWelcomeMail(@Nonnull final String recipient) {
    mailResource.get().sendWelcomeMail(recipient);
  }

  public void sendCourseMails(@Nonnull final MailContentDto mailContent,
                              @Nonnull final List<PlaceholderValue<CourseMailPlaceholderContext>> placeholderValues,
                              @Nullable final List<ParticipantDto> participants,
                              @Nullable final CourseScheduleDto schedule,
                              @Nullable final CourseDto course) {
    mailResource.get().sendCourseMails(mailContent, placeholderValues, participants, schedule, course);
  }

}
