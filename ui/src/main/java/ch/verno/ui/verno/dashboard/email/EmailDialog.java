package ch.verno.ui.verno.dashboard.email;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import ch.verno.common.db.service.mail.IMailTemplateService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.servergate.MailServerGate;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.MailTemplateType;
import ch.verno.common.lib.mail.placeholder.Placeholder;
import ch.verno.common.lib.mail.placeholder.PlaceholderValue;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Lazy;
import ch.verno.server.async.BackgroundExecutor;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.mail.SendMailPopup;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class EmailDialog extends VADialog {

  @Nonnull private final IMailTemplateService mailTemplateService;
  @Nonnull private final Lazy<MailServerGate> mailServerGate;
  @Nonnull private final Binder<MailTemplateDto> binder;
  @Nonnull private final EntryFactory<MailTemplateDto> entryFactory;
  @Nonnull private final MailTemplateType mailTemplateType;

  @Nullable private TextArea selectedTextArea;

  private List<ParticipantDto> participants;

  public EmailDialog(@Nonnull final GlobalInterface globalInterface,
                     @Nonnull final MailTemplateType mailTemplateType) {
    this.mailTemplateService = globalInterface.getService(IMailTemplateService.class);
    this.mailServerGate = Lazy.of(() -> globalInterface.getService(MailServerGate.class));
    this.binder = new Binder<>(MailTemplateDto.class);
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.mailTemplateType = mailTemplateType;

    if (mailTemplateService.hasTemplateByKey(mailTemplateType.getKey())) {
      final var templateByKey = mailTemplateService.getTemplateByKey(mailTemplateType.getKey());
      binder.setBean(templateByKey);
    } else {
      binder.setBean(new MailTemplateDto(mailTemplateType.getKey()));
    }

    initUI(getTranslation("setting.send.email"), DialogSize.BIG);
    selectedTextArea = null;
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    final var bean = binder.getBean();
    if (bean != null) {
      binder.readBean(bean);
    }
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var emailSubject = entryFactory.createTextAreaEntry(
            MailTemplateDto::getSubject,
            MailTemplateDto::setSubject,
            binder,
            Optional.of(getTranslation("setting.email.subject.is.required")),
            getTranslation("setting.email.subject")
    );
    emailSubject.setWidthFull();
    emailSubject.addFocusListener(event -> selectedTextArea = emailSubject);
    emailSubject.setPlaceholder(getTranslation("setting.no.email.subject.yet.use.the.buttons.on.the.right.to.add.placeholders.and.type.your.own.subject"));

    final var emailContent = entryFactory.createTextAreaEntry(
            MailTemplateDto::getContent,
            MailTemplateDto::setContent,
            binder,
            Optional.of(getTranslation("setting.email.content.is.required")),
            getTranslation("setting.email.content")
    );
    emailContent.addFocusListener(event -> selectedTextArea = emailContent);
    emailContent.setPlaceholder(getTranslation("setting.no.email.content.yet.use.the.buttons.on.the.right.to.add.placeholders.and.type.your.own.content"));
    emailContent.setWidthFull();
    emailContent.setHeightFull();
    emailContent.setSizeFull();

    final var emailLayout = new VerticalLayout(emailSubject, emailContent);
    emailLayout.setPadding(false);
    emailLayout.setSpacing(false);
    emailLayout.getStyle().set("min-width", "0");
    emailLayout.setWidthFull();
    emailLayout.setHeightFull();

    final var placeholderLayout = createPlaceholderLayout();

    placeholderLayout.setWidth(null);
    placeholderLayout.setFlexShrink(0);

    final var contentLayout = new HorizontalLayout(emailLayout, placeholderLayout);
    contentLayout.setSizeFull();
    contentLayout.setPadding(false);
    contentLayout.setSpacing(true);

    contentLayout.setFlexGrow(1, emailLayout);
    contentLayout.setFlexGrow(0, placeholderLayout);

    return contentLayout;
  }

  @Nonnull
  private VerticalLayout createPlaceholderLayout() {
    final var firstname = createPlaceholderButton(Placeholder.FIRSTNAME);
    final var lastname = createPlaceholderButton(Placeholder.LASTNAME);

    return new VerticalLayout(firstname, lastname);
  }

  @Nonnull
  private Button createPlaceholderButton(@Nonnull final Placeholder placeholder) {
    final var placeholderButton = new Button(getTranslation(placeholder.getNameKey()));
    placeholderButton.addClickListener(e -> {
      if (selectedTextArea != null) {
        selectedTextArea.setValue(selectedTextArea.getValue() + placeholder.getValue());
        selectedTextArea.focus();
      }
    });
    return placeholderButton;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
    final var downloadButton = createSendButton();

    return List.of(cancelButton, downloadButton);
  }

  @Nonnull
  private Button createSendButton() {
    final var sendButton = new Button(getTranslation("shared.send"));
    sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    sendButton.addClickListener(e -> sendEmail());
    return sendButton;
  }

  public void sendEmail() {
    close();

    final var bean = binder.getBean();
    final var mailContent = new MailContentDto(bean.getSubject(), bean.getContent());

    final var ui = UI.getCurrent();
    if (ui == null) {
      mailServerGate.get().sendCourseEmails(mailContent, List.of(), participants);
      return;
    }

    final var popup = new SendMailPopup(
            getTranslation("shared.send"),
            getTranslation("setting.send.email")
    );

    popup.openAndRunAsync(
            ui,
            TenantContext.getRequired(),
            BackgroundExecutor.getInstance().getExecutorService(),
            () -> mailServerGate.get().sendCourseEmails(
                    mailContent,
                    getPlaceHolderValues(),
                    participants != null ? participants : List.of()
            ),
            () -> NotificationFactory.showSuccessNotification(getTranslation("mail.e.mail.s.sent.successfully")),
            () -> NotificationFactory.showErrorNotification(getTranslation("mail.e.mail.s.sent.failed"))
    );
  }

  @Nonnull
  private List<PlaceholderValue<ParticipantDto>> getPlaceHolderValues() {
    final var placeholderValues = new ArrayList<PlaceholderValue<ParticipantDto>>();
    placeholderValues.add(new PlaceholderValue<>(Placeholder.FIRSTNAME, ParticipantDto::getFirstName));
    placeholderValues.add(new PlaceholderValue<>(Placeholder.LASTNAME, ParticipantDto::getLastName));
    return placeholderValues;
  }

  @Override
  public void close() {
    if (mailTemplateService.hasTemplateByKey(mailTemplateType.getKey())) {
      final var templateToUpdate = mailTemplateService.getTemplateByKey(mailTemplateType.getKey());
      templateToUpdate.setSubject(binder.getBean().getSubject());
      templateToUpdate.setContent(binder.getBean().getContent());
      mailTemplateService.upsertTemplate(templateToUpdate);
    } else {
      final var bean = binder.getBean();
      mailTemplateService.upsertTemplate(bean);
    }

    super.close();
  }


  public void setParticipants(@Nonnull final List<ParticipantDto> participants) {
    this.participants = participants;
  }
}
