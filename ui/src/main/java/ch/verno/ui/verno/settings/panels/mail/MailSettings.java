package ch.verno.ui.verno.settings.panels.mail;

import ch.verno.common.db.dto.table.mail.MailConfigDto;
import ch.verno.common.db.enums.mail.SmtpSecurity;
import ch.verno.common.db.service.mail.IMailConfigService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class MailSettings extends VABaseSetting<MailConfigDto> {

  public static final String TITLE_KEY = "setting.mail.provider.settings";

  @Nonnull private final IMailConfigService mailConfigService;
  @Nonnull private final EntryFactory<MailConfigDto> entryFactory;

  public MailSettings(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, TITLE_KEY, true);

    mailConfigService = globalInterface.getService(IMailConfigService.class);
    entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());

    if (mailConfigService.hasConfigForCurrentTenant()) {
      dto = mailConfigService.getConfigForCurrentTenant();
    }
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    addActionButtons(saveButton, getActionButton());
  }

  @Nonnull
  private Button getActionButton() {
    final var button = new Button(VaadinIcon.ELLIPSIS_V.create());

    final var menu = new ContextMenu();
    menu.setTarget(button);
    menu.setOpenOnClick(true);

    menu.addItem("Test Connection", e -> {
      final var dialog = new TestConnectionDialog(globalInterface);
      dialog.open();
    });

    return button;
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var fromEmail = entryFactory.createEmailEntry(
            MailConfigDto::getFromEmail,
            MailConfigDto::setFromEmail,
            binder,
            Optional.of("From Email is required"),
            "From Email"
    );
    final var fromName = entryFactory.createTextField(
            MailConfigDto::getFromName,
            MailConfigDto::setFromName,
            binder,
            Optional.of("From Name is required"),
            "From Name"
    );
    final var replyToEmail = entryFactory.createEmailEntry(
            MailConfigDto::getReplyToEmail,
            MailConfigDto::setReplyToEmail,
            binder,
            Optional.empty(),
            "Reply-To Email"
    );
    final var defaultBcc = entryFactory.createEmailEntry(
            MailConfigDto::getDefaultBcc,
            MailConfigDto::setDefaultBcc,
            binder,
            Optional.empty(),
            "Default BCC"
    );

    final var smtpHost = entryFactory.createTextField(
            MailConfigDto::getSmtpHost,
            MailConfigDto::setSmtpHost,
            binder,
            Optional.of("SMTP Host is required"),
            "SMTP Host"
    );
    final var smtpUsername = entryFactory.createTextField(
            MailConfigDto::getSmtpUsername,
            MailConfigDto::setSmtpUsername,
            binder,
            Optional.of("SMTP Username is required"),
            "SMTP Username"
    );
    final var smtpPassword = entryFactory.createPasswordField(
            MailConfigDto::getSmtpPasswordB64,
            MailConfigDto::setSmtpPasswordB64,
            binder,
            Optional.of("SMTP Password is required"),
            "SMTP Password"
    );

    final var smtpSecurityOptions = Arrays.stream(SmtpSecurity.values())
            .collect(Collectors.toMap(
                    SmtpSecurity::getId,
                    SmtpSecurity::getDisplayName,
                    (a, b) -> a,
                    LinkedHashMap::new
            ));
    final var smtpSecurity = entryFactory.createComboBoxEntry(
            dto -> dto.getSmtpSecurity().getId(),
            (dto, id) -> dto.setSmtpSecurity(SmtpSecurity.fromId(id)),
            binder,
            Optional.of("SMTP Security is required"),
            "SMTP Security",
            smtpSecurityOptions
    );

    return LayoutUtil.createHorizontal(
            fromEmail,
            fromName,
            replyToEmail,
            defaultBcc,
            smtpHost,
            smtpUsername,
            smtpPassword,
            smtpSecurity
    );
  }

  @Nonnull
  @Override
  protected Class<MailConfigDto> getBeanType() {
    return MailConfigDto.class;
  }

  @Nonnull
  @Override
  protected MailConfigDto createNewBeanInstance() {
    return new MailConfigDto();
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      final var saved = mailConfigService.upsertConfig(dto);
    }
  }
}
