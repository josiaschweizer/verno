package ch.verno.ui.verno.dashboard.mail;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailTemplateType;
import ch.verno.ui.lib.components.email.dialog.AbstractMailDialog;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class CourseMailDialog extends AbstractMailDialog<CourseMailTemplateConfigLayout> {

  @Nullable private CourseDto course;
  @Nullable private List<ParticipantDto> participants;
  @Nullable private CourseScheduleDto courseSchedule;

  public CourseMailDialog(@Nonnull final Injector injector,
                          @Nonnull final MailTemplateType mailTemplateType) {
    super(injector, mailTemplateType);
  }

  @Override
  @Nonnull
  protected CourseMailTemplateConfigLayout createTemplateConfigLayout(@Nonnull final GlobalInterface globalInterface,
                                                                      @Nonnull final MailTemplateType mailTemplateType) {
    return new CourseMailTemplateConfigLayout(globalInterface, mailTemplateType);
  }

  @Override
  protected void executeSend(@Nonnull final MailContentDto mailContent) {
    mailServerGate.get().sendCourseEmails(
            mailContent,
            templateConfigLayout.getPlaceholderValues(),
            participants != null ? participants : List.of(),
            courseSchedule,
            course
    );
  }

  public void setParticipants(@Nonnull final List<ParticipantDto> participants) {
    this.participants = participants;
  }

  public void setCourseSchedule(@Nullable final CourseScheduleDto courseSchedule) {
    this.courseSchedule = courseSchedule;
  }

  public void setCourse(@Nullable final CourseDto course) {
    this.course = course;

    if (course != null) {
      this.courseSchedule = course.getCourseSchedule();
    }
  }
}