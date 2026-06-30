package ch.verno.contract.endpoint.mail;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailDto;
import ch.verno.contract.mail.placeholder.PlaceholderValue;
import ch.verno.contract.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

@RpcEndpoint
public interface MailResource {

  void sendMail(@Nonnull MailDto mailDto);

  void sendWelcomeMail(@Nonnull String recipient);

  void sendCourseMails(@Nonnull MailContentDto mailContent,
                       @Nonnull List<PlaceholderValue<CourseMailPlaceholderContext>> placeholderValues,
                       @Nullable List<ParticipantDto> participants,
                       @Nullable CourseScheduleDto schedule,
                       @Nullable CourseDto course);
}
