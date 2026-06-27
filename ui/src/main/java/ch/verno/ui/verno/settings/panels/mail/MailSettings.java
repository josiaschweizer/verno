package ch.verno.ui.verno.settings.panels.mail;

import ch.verno.common.dto.ui.badge.VABadgeLabelOptions;
import ch.verno.common.type.mail.MailValidity;
import ch.verno.common.type.mail.SmtpSecurity;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.mail.MailConfigClient;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.event.ReloadNavigationBarEvent;
import ch.verno.ui.lib.event.bus.ViewEventBus;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.util.LayoutUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class MailSettings extends VABaseSetting<MailConfigDto> {

  public static final String TITLE_KEY = "setting.mail.provider.settings";

  @Nonnull private final Lazy<MailConfigClient> mailConfigClient;
  @Nonnull private final EntryFactory<MailConfigDto> entryFactory;

  @Inject
  public MailSettings(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);

    this.mailConfigClient = Lazy.of(() -> injector.getInstance(MailConfigClient.class));
    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));

    if (mailConfigClient.get().hasMailConfigForCurrentTenant()) {
      this.dto = getConfigForCurrentTenant();
    }
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    addActionButtons(saveButton, getActionButton());
    updateHeaderBadge();
  }

  @Nonnull
  private Button getActionButton() {
    final var button = new Button(VaadinIcon.ELLIPSIS_V.create());

    final var menu = new ContextMenu();
    menu.setTarget(button);
    menu.setOpenOnClick(true);

    menu.addItem(getTranslation("setting.test.connection"), e -> {
      save(); // save the current config to then load it into the dialog

      final var dialog = new TestConnectionDialog(injector);
      dialog.addClosedListener(close -> {
        // refresh config after test connection dialog is closed, as the test dialog might update the mail validity
        if (mailConfigClient.get().hasMailConfigForCurrentTenant()) {
          dto = getConfigForCurrentTenant();
          binder.readBean(dto);
          updateHeaderBadge();
          updateNavigationBar();
        }
      });
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
            Optional.of(getTranslation("setting.from.email.is.required")),
            getTranslation("setting.from.email")
    );
    final var fromName = entryFactory.createTextField(
            MailConfigDto::getFromName,
            MailConfigDto::setFromName,
            binder,
            Optional.of(getTranslation("setting.from.name.is.required")),
            getTranslation("setting.from.name")
    );
    final var replyToEmail = entryFactory.createEmailEntry(
            MailConfigDto::getReplyToEmail,
            MailConfigDto::setReplyToEmail,
            binder,
            Optional.empty(),
            getTranslation("setting.reply.to.email")
    );
    final var defaultBcc = entryFactory.createEmailEntry(
            MailConfigDto::getDefaultBcc,
            MailConfigDto::setDefaultBcc,
            binder,
            Optional.empty(),
            getTranslation("setting.default.bcc")
    );

    final var smtpHost = entryFactory.createTextField(
            MailConfigDto::getSmtpHost,
            MailConfigDto::setSmtpHost,
            binder,
            Optional.of(getTranslation("setting.smtp.host.is.required")),
            getTranslation("setting.smtp.host")
    );
    final var smtpUsername = entryFactory.createTextField(
            MailConfigDto::getSmtpUsername,
            MailConfigDto::setSmtpUsername,
            binder,
            Optional.of(getTranslation("setting.smtp.username.is.required")),
            getTranslation("setting.smtp.username")
    );
    final var smtpPassword = entryFactory.createPasswordField(
            MailConfigDto::getDecodedSmtpPassword,
            MailConfigDto::setDecodedPasswordB64,
            binder,
            Optional.of(getTranslation("setting.smtp.password.is.required")),
            getTranslation("setting.smtp.password")
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
            Optional.of(getTranslation("setting.smtp.security.is.required")),
            getTranslation("setting.smtp.security"),
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
    return MailConfigDto.empty();
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      mailConfigClient.get().saveMailConfig(dto);
      updateNavigationBar();
    }
  }

  @Override
  protected void binderValueChanged() {
    super.binderValueChanged();
    applyBinderValues();
    updateEntity();
    updateHeaderBadge();
  }

  private void applyBinderValues() {
    binder.writeBeanAsDraft(dto);
  }

  private void updateEntity() {
    if (binder.hasChanges()) {
      dto.setMailValidity(MailValidity.UNTESTED);
    }
  }

  private void updateHeaderBadge() {
    VABadgeLabel badge;
    if (dto.isEmpty()) {
      badge = createNotConfiguredBadge();
    } else {
      badge = switch (dto.getMailValidity()) {
        case TESTED_VALID -> createValidHeaderBadge();
        case TESTED_INVALID -> createInvalidHeaderBadge();
        case UNTESTED -> createUntestedHeaderBadge();
      };

    }

    setHeaderBadge(badge);
  }

  private void updateNavigationBar() {
    ViewEventBus.getInstance().post(new ReloadNavigationBarEvent());
  }

  @Nonnull
  private VABadgeLabel createValidHeaderBadge() {
    return BadgeLabelFactory.createBadgeLabel(getTranslation("setting.valid.configuration"), VABadgeLabelOptions.SUCCESS);
  }

  @Nonnull
  private VABadgeLabel createInvalidHeaderBadge() {
    return BadgeLabelFactory.createBadgeLabel(getTranslation("setting.invalid.configuration"), VABadgeLabelOptions.ERROR);
  }

  @Nonnull
  private VABadgeLabel createUntestedHeaderBadge() {
    return BadgeLabelFactory.createBadgeLabel(getTranslation("setting.untested.configuration"), VABadgeLabelOptions.WARNING);
  }

  @Nonnull
  private VABadgeLabel createNotConfiguredBadge() {
    return BadgeLabelFactory.createBadgeLabel(getTranslation("setting.not.configured"), VABadgeLabelOptions.NORMAL);
  }

  @Nonnull
  private MailConfigDto getConfigForCurrentTenant() {
    final var opt = mailConfigClient.get().getMailConfigForCurrentTenant();
    if (opt.isPresent()) {
      return opt.get();
    } else {
      throw new IllegalStateException("Mail config resource not found");
    }
  }
}
