package ch.verno.ui.verno.dashboard.mail;

import ch.verno.contract.mail.MailTemplateType;
import ch.verno.contract.mail.placeholder.Placeholder;
import ch.verno.contract.mail.placeholder.PlaceholderValue;
import ch.verno.contract.mail.placeholder.context.CourseMailPlaceholderContext;
import ch.verno.lib.Publ;
import ch.verno.ui.lib.components.email.AbstractMailTemplateConfigLayout;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CourseMailTemplateConfigLayout extends AbstractMailTemplateConfigLayout {

  public CourseMailTemplateConfigLayout(@Nonnull final Injector injector,
                                        @Nonnull final MailTemplateType mailTemplateType) {
    super(injector, mailTemplateType);
  }

  @Nonnull
  @Override
  protected List<Button> createPlaceholderButtons() {
    return getPlaceholderValues().stream()
            .map(PlaceholderValue::placeholder)
            .map(this::createPlaceholderButton)
            .toList();
  }

  @Nonnull
  public List<PlaceholderValue<CourseMailPlaceholderContext>> getPlaceholderValues() {
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