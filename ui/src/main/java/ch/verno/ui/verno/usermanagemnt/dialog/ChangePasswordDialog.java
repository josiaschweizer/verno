package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.rpc.properties.user.UserProperties;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.util.LayoutUtil;
import ch.verno.ui.lib.util.LogoutUtil;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ChangePasswordDialog extends VAAbstractDialog {

  @Nonnull private final Injector injector;
  @Nonnull private final AppUserClient appUserClient;

  @Nonnull private final Binder<ChangePasswordDto> binder;
  @Nonnull private final EntryFactory<ChangePasswordDto> entryFactory;

  public ChangePasswordDialog(@Nonnull final Injector injector,
                              @Nonnull final Long userId) {
    this.injector = injector;
    this.appUserClient = injector.getInstance(AppUserClient.class);

    this.binder = new Binder<>(ChangePasswordDto.class);
    this.binder.setBean(new ChangePasswordDto(userId));

    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));

    final var user = appUserClient.findByUserId(userId).orElseGet(AppUserDto::empty);
    initUI(getTranslation("shared.change.password", user.getUsername()), DialogSize.MEDIUM_COMPACT);
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {
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

    return LayoutUtil.createHorizontal(newPassword, confirmNewPassword);
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
      final var newHashedPassword = injector.getInstance(PasswordEncoder.class).encode(newRawPassword);

      if (newHashedPassword == null) {
        NotificationFactory.showErrorNotification(getTranslation("shared.failed.to.hash.the.new.password.please.try.again"));
        return;
      }

      appUserClient.changePassword(changePasswordDto.getUserId(), newHashedPassword);

      final var currentUser = appUserClient.getCurrentAppUser();
      if (currentUser.getId() != null && currentUser.getId().equals(changePasswordDto.getUserId())) {
        injector.getInstance(LogoutUtil.class).logout();
      }

      appUserClient.findByUserId(changePasswordDto.getUserId()).ifPresent(user ->
              NotificationFactory.showSuccessNotification(getTranslation("shared.password.of.user.0.has.been.changed.successfully", user.getUsername()))
      );

      close();
    }
  }
}
