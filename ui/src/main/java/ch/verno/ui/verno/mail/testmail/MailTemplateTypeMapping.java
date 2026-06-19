package ch.verno.ui.verno.mail.testmail;

import ch.verno.contract.mail.MailTemplateType;
import ch.verno.ui.i18n.TranslationHelper;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public enum MailTemplateTypeMapping {
  COURSE_INVITE(
          MailTemplateType.COURSE_INVITE,
          "mail.course.invitation",
          "mail.this.email.is.sent.when.a.participant.is.invited.to.a.course"
  )/*,
  COURSE_REMINDER(
          MailTemplateType.COURSE_REMINDER,
          "mail.course.reminder",
          "mail.this.email.is.sent.before.a.course.starts.to.remind.participants.about.the.upcoming.course"
  )*/;

  @Nonnull private final MailTemplateType mailTemplateType;
  @Nonnull private final String nameKey;
  @Nonnull private final String descriptionKey;

  MailTemplateTypeMapping(@Nonnull final MailTemplateType mailTemplateType,
                          @Nonnull final String nameKey,
                          @Nonnull final String descriptionKey) {
    this.mailTemplateType = mailTemplateType;
    this.nameKey = nameKey;
    this.descriptionKey = descriptionKey;
  }

  @Nonnull
  public MailTemplateType getMailTemplateType() {
    return mailTemplateType;
  }

  @Nonnull
  public String getName(@Nonnull final Injector injector) {
    return injector.getInstance(TranslationHelper.class).getTranslation(nameKey);
  }

  @Nonnull
  public String getDescription(@Nonnull final Injector injector) {
    return injector.getInstance(TranslationHelper.class).getTranslation(nameKey);
  }
}