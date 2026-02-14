package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.ui.dto.UserDtoUnhashedPw;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.layouts.UserLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class CreateUserDialog extends VADialog {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IAppUserService appUserService;

  @Nonnull private final EntryFactory<UserDtoUnhashedPw> entryFactory;
  @Nonnull private final Binder<UserDtoUnhashedPw> binder;

  @Nonnull private final FormMode formMode;
  @Nonnull private final String oldUserName;

  public CreateUserDialog(@Nonnull final GlobalInterface globalInterface) {
    this(globalInterface, FormMode.CREATE, new UserDtoUnhashedPw());
  }

  public CreateUserDialog(@Nonnull final GlobalInterface globalInterface,
                          @Nonnull final FormMode formMode,
                          @Nonnull final UserDtoUnhashedPw binderDto) {
    this.globalInterface = globalInterface;
    this.appUserService = globalInterface.getService(IAppUserService.class);

    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.binder = new Binder<>(UserDtoUnhashedPw.class);
    this.binder.setBean(binderDto);

    this.formMode = formMode;
    this.oldUserName = binderDto.getUsername();

    initUI(getTranslation("shared.create.new.application.user"), DialogSize.MEDIUM_COMPACT);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var userLayout = new UserLayout(globalInterface, entryFactory);
    userLayout.setPasswordReadOnly("shared.password.can.only.be.changed.via.the.change.password.dialog.via.grid.right.click.change.password");
    return userLayout.buildUserLayout(binder, formMode, oldUserName);
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var createButton = createSaveButton();
    final var cancelButton = new Button(getTranslation("shared.cancel"), event -> close());

    return List.of(cancelButton, createButton);
  }

  @Nonnull
  private Button createSaveButton() {
    final var button = new Button(formMode == FormMode.CREATE ? getTranslation("shared.create") : getTranslation("shared.update"));
    button.addClickListener(event -> save());
    button.setEnabled(false);

    binder.addValueChangeListener(event -> button.setEnabled(binder.isValid()));

    return button;
  }

  private void save() {
    final var bean = binder.getBean();
    if (bean == null) {
      return;
    }

    if (formMode == FormMode.CREATE) {
      createNewUser(bean);
    } else {
      updateUser(bean);
    }

    close();
  }

  private void createNewUser(@Nonnull final UserDtoUnhashedPw bean) {
    if (appUserService.findByUserName(bean.getUsername()).isPresent()) {
      NotificationFactory.showErrorNotification(getTranslation("shared.username.0.already.exists", bean.getUsername()));
      return;
    }

    final var hashedPassword = globalInterface.getPasswordEncoder().encode(bean.getPassword());
    if (hashedPassword == null) {
      return;
    }

    appUserService.createAppUser(new AppUserDto(
            bean.getUsername(),
            bean.getFirstname(),
            bean.getLastname(),
            bean.getEmail(),
            hashedPassword,
            bean.getRole(),
            true
    ));

    NotificationFactory.showSuccessNotification(getTranslation("shared.created.user.0.successfully", bean.getUsername()));
  }

  private void updateUser(@Nonnull final UserDtoUnhashedPw bean) {
    final var foundById = appUserService.findByUserName(oldUserName);
    if (foundById.isEmpty()) {
      NotificationFactory.showErrorNotification(getTranslation("shared.user.with.username.0.does.not.exist", bean.getUsername()));
      return;
    }

    appUserService.updateAppUser(
            new AppUserDto(
            foundById.get().getId(),
            bean.getUsername(),
            bean.getFirstname(),
            bean.getLastname(),
            bean.getEmail(),
            Publ.EMPTY_STRING,
            bean.getRole(),
            false
    ));

    final var currentUser = globalInterface.getOptionalCurrentUser();
    if (currentUser.isEmpty()) {
      globalInterface.logout(); // user has changed his own username - log him out to avoid any issues with the security context
      return;
    }

    NotificationFactory.showSuccessNotification(getTranslation("shared.updated.user.0.successfully", bean.getUsername()));
  }
}
