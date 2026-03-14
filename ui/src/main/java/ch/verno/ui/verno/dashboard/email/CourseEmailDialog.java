package ch.verno.ui.verno.dashboard.email;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.CourseScheduleDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.MailTemplateType;
import ch.verno.common.lib.mail.placeholder.Placeholder;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.lib.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.dialog.email.AbstractEmailDialog;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public class CourseEmailDialog extends AbstractEmailDialog {

  @Nullable private List<ParticipantDto> participants;
  @Nullable private CourseScheduleDto courseSchedule;
  @Nullable private CourseDto course;

  public CourseEmailDialog(@Nonnull final GlobalInterface globalInterface,
                           @Nonnull final MailTemplateType mailTemplateType) {
    super(globalInterface, mailTemplateType);
  }

  @Override
  @Nonnull
  protected List<Button> createPlaceholderButtons() {
    return getPlaceholderValues().stream()
            .map(PlaceholderValue::placeholder)
            .map(this::createPlaceholderButton)
            .toList();
  }

  @Override
  protected void executeSend(@Nonnull final MailContentDto mailContent) {
    mailServerGate.get().sendCourseEmails(
            mailContent,
            getPlaceholderValues(),
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

  @Nonnull
  private List<PlaceholderValue<CourseMailPlaceholderContext>> getPlaceholderValues() {
    return List.of(
            new PlaceholderValue<>(Placeholder.FIRSTNAME, ctx -> ctx.participant().getFirstName()),
            new PlaceholderValue<>(Placeholder.LASTNAME, ctx -> ctx.participant().getLastName()),
            new PlaceholderValue<>(Placeholder.COURSE_NAME, ctx -> ctx.course() != null ? ctx.course().getTitle() : Publ.EMPTY_STRING),
            new PlaceholderValue<>(Placeholder.COURSE_START_DATE, CourseMailPlaceholderContext::getCourseStartDate),
            new PlaceholderValue<>(Placeholder.COURSE_END_DATE, CourseMailPlaceholderContext::getCourseEndDate),
            new PlaceholderValue<>(Placeholder.COURSE_START_TIME, CourseMailPlaceholderContext::getCourseStartTime),
            new PlaceholderValue<>(Placeholder.COURSE_END_TIME, CourseMailPlaceholderContext::getCourseEndTime)
    );
  }
}