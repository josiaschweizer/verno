package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ChangePasswordDialog extends VADialog {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IAppUserService appUserService;
  @Nonnull private final EntryFactory<ChangePasswordDto> entryFactory;
  @Nonnull private final Binder<ChangePasswordDto> binder;

  public ChangePasswordDialog(@Nonnull final GlobalInterface globalInterface,
                              @Nonnull final Long userId) {
    this.globalInterface = globalInterface;
    this.appUserService = globalInterface.getService(IAppUserService.class);
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.binder = new Binder<>(ChangePasswordDto.class);
    this.binder.setBean(new ChangePasswordDto(userId));

    final var user = appUserService.findAppUserById(userId);
    initUI(getTranslation("shared.change.password", user.getUsername()), DialogSize.MEDIUM_COMPACT);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var newPassword = entryFactory.createPasswordField(
            ChangePasswordDto::getNewPassword,
            ChangePasswordDto::setNewPassword,
            binder,
            Optional.of(getTranslation("shared.new.password.is.required")),
            getTranslation("shared.new.password")
    );

    final var confirmNewPassword = entryFactory.createPasswordField(
            ChangePasswordDto::getConfirmNewPassword,
            ChangePasswordDto::setConfirmNewPassword,
            binder,
            Optional.of(getTranslation("shared.please.confirm.the.new.password")),
            getTranslation("shared.confirm.new.password")
    );

    binder.forField(confirmNewPassword)
            .withValidator(
                    value -> value != null && value.equals(binder.getBean().getNewPassword()),
                    getTranslation("shared.passwords.must.match")
            )
            .bind(ChangePasswordDto::getConfirmNewPassword, ChangePasswordDto::setConfirmNewPassword);

    return LayoutUtil.createHorizontalLayoutFromComponents(newPassword, confirmNewPassword);
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), event -> close());
    final var saveButton = createSaveButton();
    return List.of(cancelButton, saveButton);
  }

  private Button createSaveButton() {
    final var button = new Button(getTranslation(getTranslation("common.save")));
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    button.setEnabled(false);
    button.addClickListener(event -> updatePassword());

    binder.addValueChangeListener(e -> button.setEnabled(binder.isValid()));

    return button;
  }

  private void updatePassword() {
    if (binder.isValid()) {
      final var changePasswordDto = binder.getBean();
      final var newRawPassword = changePasswordDto.getNewPassword();
      final var newHashedPassword = globalInterface.getPasswordEncoder().encode(newRawPassword);

      if (newHashedPassword == null) {
        NotificationFactory.showErrorNotification(getTranslation("shared.failed.to.hash.the.new.password.please.try.again"));
        return;
      }

      appUserService.changePassword(changePasswordDto.getUserId(), newHashedPassword);

      final var currentUser = globalInterface.getCurrentUser();
      if (currentUser.getId() != null && currentUser.getId().equals(changePasswordDto.getUserId())) {
        globalInterface.logout();
      }

      final var user = appUserService.findAppUserById(changePasswordDto.getUserId());
      NotificationFactory.showSuccessNotification(getTranslation("shared.password.of.user.0.has.been.changed.successfully", user.getUsername()));

      close();
    }
  }
}
