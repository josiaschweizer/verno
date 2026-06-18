package ch.verno.ui.lib.components.email.dialog;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.MailServerGate;
import ch.verno.contract.mail.MailContentDto;
import ch.verno.contract.mail.MailTemplateType;
import ch.verno.common.tenant.TenantContext;
import ch.verno.lib.Lazy;
import ch.verno.server.async.BackgroundExecutor;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.lib.components.email.AbstractMailTemplateConfigLayout;
import ch.verno.ui.lib.mail.SendMailPopup;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public abstract class AbstractMailDialog<T extends AbstractMailTemplateConfigLayout> extends VAAbstractDialog {

  @Nonnull protected final Lazy<MailServerGate> mailServerGate;
  @Nonnull protected final T templateConfigLayout;

  private boolean isCanceled;

  public AbstractMailDialog(@Nonnull final GlobalInterface globalInterface,
                            @Nonnull final MailTemplateType mailTemplateType) {
    this.mailServerGate = Lazy.of(() -> globalInterface.getService(MailServerGate.class));
    this.templateConfigLayout = createTemplateConfigLayout(globalInterface, mailTemplateType);

    initUI(getTranslation("setting.send.email"), DialogSize.BIG);

    this.isCanceled = false;
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var layout = new HorizontalLayout(templateConfigLayout);
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    layout.setFlexGrow(1, templateConfigLayout);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of(
            new Button(getTranslation("shared.cancel"), e -> {
              this.isCanceled = true;
              close();
            }),
            createSendButton()
    );
  }

  public void sendEmail() {
    if (!templateConfigLayout.isValid()) {
      return;
    }

    final var bean = templateConfigLayout.getBean();
    final var mailContent = new MailContentDto(bean.getSubject(), bean.getContent());

    close();

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
    super.close();

    if (!isCanceled) {
      templateConfigLayout.saveTemplate();
    }
  }

  @Nonnull
  private Button createSendButton() {
    final var btn = new Button(getTranslation("shared.send"));
    btn.setEnabled(templateConfigLayout.isValid());
    btn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    btn.addClickListener(e -> sendEmail());

    templateConfigLayout.addStatusChangeListener(() -> btn.setEnabled(templateConfigLayout.isValid()));

    return btn;
  }

  @Nonnull
  protected abstract T createTemplateConfigLayout(@Nonnull GlobalInterface globalInterface,
                                                  @Nonnull MailTemplateType mailTemplateType);

  protected abstract void executeSend(@Nonnull MailContentDto mailContent);
}