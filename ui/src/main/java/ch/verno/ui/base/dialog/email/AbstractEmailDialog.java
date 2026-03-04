package ch.verno.ui.base.dialog.email;

import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import ch.verno.common.db.service.mail.IMailTemplateService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.servergate.MailServerGate;
import ch.verno.common.lib.mail.MailContentDto;
import ch.verno.common.lib.mail.MailTemplateType;
import ch.verno.common.lib.mail.placeholder.Placeholder;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Lazy;
import ch.verno.server.async.BackgroundExecutor;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.mail.SendMailPopup;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEmailDialog extends VADialog {

  @Nonnull private final IMailTemplateService mailTemplateService;
  @Nonnull protected final Lazy<MailServerGate> mailServerGate;
  @Nonnull protected final Binder<MailTemplateDto> binder;
  @Nonnull private final EntryFactory<MailTemplateDto> entryFactory;
  @Nonnull private final MailTemplateType mailTemplateType;

  @Nullable private TextArea selectedTextArea;

  public AbstractEmailDialog(@Nonnull final GlobalInterface globalInterface,
                             @Nonnull final MailTemplateType mailTemplateType) {
    this.mailTemplateService = globalInterface.getService(IMailTemplateService.class);
    this.mailServerGate = Lazy.of(() -> globalInterface.getService(MailServerGate.class));
    this.binder = new Binder<>(MailTemplateDto.class);
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.mailTemplateType = mailTemplateType;

    if (mailTemplateService.hasTemplateByKey(mailTemplateType.getKey())) {
      binder.setBean(mailTemplateService.getTemplateByKey(mailTemplateType.getKey()));
    } else {
      binder.setBean(new MailTemplateDto(mailTemplateType.getKey()));
    }

    initUI(getTranslation("setting.send.email"), DialogSize.BIG);
    selectedTextArea = null;
  }

  @Nonnull
  protected abstract List<Button> createPlaceholderButtons();

  protected abstract void executeSend(@Nonnull MailContentDto mailContent);

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    final var bean = binder.getBean();
    if (bean != null) binder.readBean(bean);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var emailSubject = entryFactory.createTextAreaEntry(
            MailTemplateDto::getSubject, MailTemplateDto::setSubject, binder,
            Optional.of(getTranslation("setting.email.subject.is.required")),
            getTranslation("setting.email.subject")
    );
    emailSubject.setWidthFull();
    emailSubject.addFocusListener(e -> selectedTextArea = emailSubject);
    emailSubject.setPlaceholder(getTranslation("setting.no.email.subject.yet..."));

    final var emailContent = entryFactory.createTextAreaEntry(
            MailTemplateDto::getContent, MailTemplateDto::setContent, binder,
            Optional.of(getTranslation("setting.email.content.is.required")),
            getTranslation("setting.email.content")
    );
    emailContent.addFocusListener(e -> selectedTextArea = emailContent);
    emailContent.setPlaceholder(getTranslation("setting.no.email.content.yet..."));
    emailContent.setSizeFull();

    final var emailLayout = new VerticalLayout(emailSubject, emailContent);
    emailLayout.setPadding(false);
    emailLayout.setSpacing(false);
    emailLayout.getStyle().set("min-width", "0");
    emailLayout.setSizeFull();

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
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of(
            new Button(getTranslation("shared.cancel"), e -> close()),
            createSendButton()
    );
  }

  public void sendEmail() {
    close();
    final var bean = binder.getBean();
    final var mailContent = new MailContentDto(bean.getSubject(), bean.getContent());

    final var ui = UI.getCurrent();
    if (ui == null) {
      executeSend(mailContent);
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
            () -> executeSend(mailContent),
            () -> NotificationFactory.showSuccessNotification(getTranslation("mail.e.mail.s.sent.successfully")),
            () -> NotificationFactory.showErrorNotification(getTranslation("mail.e.mail.s.sent.failed"))
    );
  }

  @Override
  public void close() {
    final var bean = binder.getBean();
    if (mailTemplateService.hasTemplateByKey(mailTemplateType.getKey())) {
      final var template = mailTemplateService.getTemplateByKey(mailTemplateType.getKey());
      template.setSubject(bean.getSubject());
      template.setContent(bean.getContent());
      mailTemplateService.upsertTemplate(template);
    } else {
      mailTemplateService.upsertTemplate(bean);
    }
    super.close();
  }

  @Nonnull
  protected Button createPlaceholderButton(@Nonnull final Placeholder placeholder) {
    final var btn = new Button(getTranslation(placeholder.getNameKey()));
    btn.addClickListener(e -> {
      if (selectedTextArea != null) {
        selectedTextArea.setValue(selectedTextArea.getValue() + placeholder.getValue());
        selectedTextArea.focus();
      }
    });
    return btn;
  }

  @Nonnull
  private VerticalLayout createPlaceholderLayout() {
    return new VerticalLayout(createPlaceholderButtons().toArray(new Component[0]));
  }

  @Nonnull
  private Button createSendButton() {
    final var btn = new Button(getTranslation("shared.send"));
    btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btn.addClickListener(e -> sendEmail());
    return btn;
  }
}