package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.factory.EntryFactory;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.*;

public class CreateUserDialog extends VADialog {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IAppUserService appUserService;

  @Nonnull private final EntryFactory<CreateUserDto> entryFactory;
  @Nonnull private final Binder<CreateUserDto> binder;

  @Nonnull private final FormMode formMode;
  @Nonnull private final String oldUserName;

  public CreateUserDialog(@Nonnull final GlobalInterface globalInterface) {
    this(globalInterface, FormMode.CREATE, new CreateUserDto());
  }

  public CreateUserDialog(@Nonnull final GlobalInterface globalInterface,
                          @Nonnull final FormMode formMode,
                          @Nonnull final CreateUserDto binderDto) {
    this.globalInterface = globalInterface;
    this.appUserService = globalInterface.getService(IAppUserService.class);

    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.binder = new Binder<>(CreateUserDto.class);
    this.binder.setBean(binderDto);

    this.formMode = formMode;
    this.oldUserName = binderDto.getUsername();

    initUI(getTranslation("shared.create.new.application.user"), DialogSize.MEDIUM_COMPACT);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var username = entryFactory.createTextEntry(
            CreateUserDto::getUsername,
            CreateUserDto::setUsername,
            binder,
            Optional.of(getTranslation("shared.username.is.required")),
            getTranslation("shared.username")
    );
    final var password = entryFactory.createPasswordField(
            CreateUserDto::getPassword,
            CreateUserDto::setPassword,
            binder,
            Optional.of(getTranslation("shared.password.is.required")),
            getTranslation("shared.password")
    );

    if (formMode != FormMode.CREATE) {
      password.setValue("********"); // show placeholder with 8 positions instead of actual password
      password.setReadOnly(true);
      password.setTooltipText(getTranslation("shared.password.can.only.be.changed.via.the.change.password.dialog.via.grid.right.click.change.password"));
    }

    final var role = entryFactory.createEnumComboBoxEntry(
            CreateUserDto::getRole,
            CreateUserDto::setRole,
            binder,
            Arrays.stream(Role.values())
                    .sorted(Comparator.comparing(Role::getId).reversed())
                    .toArray(Role[]::new),
            Optional.of(getTranslation("shared.role.is.required")),
            getTranslation("shared.role"),
            Role::getRoleNameKey
    );

    return createHorizontalLayoutFromComponents(username, password, role);
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var createButton = createSaveButton();
    final var cancelButton = new Button("Cancel", event -> close());

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

  private void createNewUser(@Nonnull final CreateUserDto bean) {
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
            hashedPassword,
            bean.getRole(),
            true
    ));

    NotificationFactory.showSuccessNotification(getTranslation("shared.created.user.0.successfully", bean.getUsername()));
  }

  private void updateUser(@Nonnull final CreateUserDto bean) {
    final var foundById = appUserService.findByUserName(oldUserName);
    if (foundById.isEmpty()) {
      NotificationFactory.showErrorNotification(getTranslation("shared.user.with.username.0.does.not.exist", bean.getUsername()));
      return;
    }

    appUserService.updateAppUser(new AppUserDto(
            foundById.get().getId(),
            bean.getUsername(),
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
