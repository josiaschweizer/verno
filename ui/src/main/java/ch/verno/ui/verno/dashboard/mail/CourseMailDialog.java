package ch.verno.ui.verno.dashboard.mail;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.MailTemplateType;
import ch.verno.ui.lib.components.email.dialog.AbstractMailDialog;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class CourseMailDialog extends AbstractMailDialog<CourseMailTemplateConfigLayout> {

  @Nullable private List<ParticipantDto> participants;
  @Nullable private CourseScheduleDto courseSchedule;
  @Nullable private CourseDto course;

  public CourseMailDialog(@Nonnull final GlobalInterface globalInterface,
                          @Nonnull final MailTemplateType mailTemplateType) {
    super(globalInterface, mailTemplateType);
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