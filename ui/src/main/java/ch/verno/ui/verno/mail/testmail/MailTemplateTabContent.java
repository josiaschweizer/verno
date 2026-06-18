package ch.verno.ui.verno.mail.testmail;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.MailServerGate;
import ch.verno.ui.i18n.TranslationHelper;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.common.test.lib.TestDataUtil;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.button.variants.VASaveButton;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.components.layout.vertical.VAVerticalLayout;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.lib.icon.IconUtil;
import ch.verno.ui.verno.dashboard.mail.CourseMailTemplateConfigLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;

public class MailTemplateTabContent {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final MailTemplateTypeMapping textMapping;
  @Nonnull private final Lazy<MailServerGate> mailServerGate;

  @Nonnull private final VAVerticalLayout layout;
  @Nullable private CourseMailTemplateConfigLayout templateLayout;

  @Nullable private TestMailTemplateDialog testMailTemplateDialog;

  public MailTemplateTabContent(@Nonnull final GlobalInterface globalInterface,
                                @Nonnull final MailTemplateTypeMapping mapping) {
    this.globalInterface = globalInterface;
    this.textMapping = mapping;
    this.mailServerGate = Lazy.of(() -> globalInterface.getService(MailServerGate.class));

    this.layout = initUI();
  }

  @Nonnull
  private VAVerticalLayout initUI() {
    final var notification = createInlineNotification();
    this.templateLayout = createMailTemplateLayout();
    final var actionLayout = createActionLayout();

    final var layout = new VAVerticalLayout(notification, templateLayout, actionLayout);
    layout.setSpacing(true);
    layout.setSizeFull();
    return layout;
  }

  @Nonnull
  private VAInlineNotification createInlineNotification() {
    final var notification = new VAInlineNotification();
    notification.setTitle(textMapping.getName(globalInterface));
    notification.setDescription(textMapping.getDescription(globalInterface));
    return notification;
  }

  @Nonnull
  private CourseMailTemplateConfigLayout createMailTemplateLayout() {
    final var config = new CourseMailTemplateConfigLayout(globalInterface, textMapping.getMailTemplateType());
    config.getContent().setSizeFull();
    return config;
  }

  @Nonnull
  private VAHorizontalLayout createActionLayout() {
    final var testMailButton = createTestMailButton();
    final var saveButton = createSaveButton();

    final var layout = new VAHorizontalLayout(testMailButton, saveButton);
    layout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setSpacing(true);
    layout.setWidthFull();

    return layout;
  }

  @Nonnull
  public VAVerticalLayout getLayout() {
    return layout;
  }

  @Nonnull
  private VAButton createTestMailButton() {
    final var button = new VAButton(TranslationHelper.getTranslation(globalInterface, "mail.test.mail.template"), IconUtil.createExtraSmall(VaadinIcon.PLAY));
    templateLayout.addBinderValueChangeListener(e1 -> button.setEnabled(templateLayout.isValid()));
    button.setEnabled(templateLayout.isValid());
    button.addClickListener(e -> testMail());
    return button;
  }

  @Nonnull
  private VAButton createSaveButton() {
    final var button = new VASaveButton(() -> templateLayout.hasChanges() && templateLayout.isValid());
    templateLayout.addBinderValueChangeListener(e -> {
      button.setEnabled(templateLayout.isValid());
      button.refreshDirtyState();
    });
    button.addClickListener(e -> {
      saveTemplate();
      button.refreshDirtyState();
    });
    button.setEnabled(templateLayout.isValid());
    return button;
  }

  private void testMail() {
    this.testMailTemplateDialog = new TestMailTemplateDialog(this::sendEmail);
    testMailTemplateDialog.open();
  }

  private void sendEmail(@Nonnull final String recipient) {
    if (textMapping == MailTemplateTypeMapping.COURSE_INVITE) {
      sendCourseInviteMail(recipient);
    }
  }

  private void sendCourseInviteMail(@Nonnull final String recipient) {
    final var demoParticipant = TestDataUtil.createDemoParticipant(recipient);
    final var courseSchedule = TestDataUtil.createDemoCourseSchedule();
    final var course = TestDataUtil.createDemoCourse();

    final var bean = templateLayout.getBean();
    final var mailContent = new MailContentDto(bean.getSubject(), bean.getContent());

    mailServerGate.get().sendCourseEmails(
            mailContent,
            templateLayout.getPlaceholderValues(),
            New.list(demoParticipant),
            courseSchedule,
            course
    );

    NotificationFactory.showSuccessNotification(TranslationHelper.getTranslation(globalInterface, "mail.email.sent.successfully"));

    if (testMailTemplateDialog != null) {
      testMailTemplateDialog.close();
    }
  }

  public void saveTemplate() {
    templateLayout.saveTemplate();
  }

}
