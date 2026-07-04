package ch.verno.ui.verno.dashboard.mail;

import ch.verno.contract.mail.MailTemplateType;
import ch.verno.contract.mail.placeholder.base.Placeholder;
import ch.verno.contract.mail.placeholder.course.CoursePlaceholder;
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
            .map(this::createPlaceholderButton)
            .toList();
  }

  @Nonnull
  public List<CoursePlaceholder> getPlaceholderValues() {
    return List.of(
            CoursePlaceholder.FIRSTNAME,
            CoursePlaceholder.LASTNAME,
            CoursePlaceholder.COURSE_NAME,
            CoursePlaceholder.COURSE_START_DATE,
            CoursePlaceholder.COURSE_END_DATE,
            CoursePlaceholder.COURSE_START_TIME,
            CoursePlaceholder.COURSE_END_TIME
    );
  }
}