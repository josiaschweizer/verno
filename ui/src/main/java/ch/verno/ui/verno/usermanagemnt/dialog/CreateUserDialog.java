package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.layouts.UserLayout;
import ch.verno.ui.lib.util.LogoutUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class CreateUserDialog extends VAAbstractDialog {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<AppUserClient> appUserClient;

  @Nonnull private final Binder<UserDtoUnhashedPw> binder;
  @Nonnull private final EntryFactory<UserDtoUnhashedPw> entryFactory;

  @Nonnull private final FormMode formMode;
  @Nonnull private final String oldUserName;

  @Inject
  public CreateUserDialog(@Nonnull final Injector injector) {
    this(injector, FormMode.CREATE, new UserDtoUnhashedPw());
  }

  public CreateUserDialog(@Nonnull final Injector injector,
                          @Nonnull final FormMode formMode,
                          @Nonnull final UserDtoUnhashedPw binderDto) {
    this.injector = injector;
    this.appUserClient = Lazy.of(() -> injector.getInstance(AppUserClient.class));

    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));
    this.binder = new Binder<>(UserDtoUnhashedPw.class);
    this.binder.setBean(binderDto);

    this.formMode = formMode;
    this.oldUserName = binderDto.getUsername();

    initUI(getTranslation("shared.create.new.application.user"), DialogSize.MEDIUM_COMPACT);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var userLayout = injector.getInstance(UserLayout.class);

    if (formMode != FormMode.CREATE) {
      setHeaderTitle(getTranslation("shared.update.existing.application.user"));
      userLayout.setPasswordReadOnly("shared.password.can.only.be.changed.via.the.change.password.dialog.via.grid.right.click.change.password");
    }

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
    final var button = new Button(formMode == FormMode.CREATE ?
            getTranslation("shared.create") :
            getTranslation("shared.update")
    );
    button.addClickListener(event -> save());
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
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
    if (appUserClient.get().findByUsername(bean.getUsername()).isPresent()) {
      NotificationFactory.showErrorNotification(getTranslation("shared.username.0.already.exists", bean.getUsername()));
      return;
    }

    appUserClient.get().saveUser(bean);

    NotificationFactory.showSuccessNotification(getTranslation("shared.created.user.0.successfully", bean.getUsername()));
  }

  private void updateUser(@Nonnull final UserDtoUnhashedPw bean) {
    final var foundById = appUserClient.get().findByUsername(oldUserName);
    if (foundById.isEmpty()) {
      NotificationFactory.showErrorNotification(getTranslation("shared.user.with.username.0.does.not.exist", bean.getUsername()));
      return;
    }

    appUserClient.get().saveUser(bean);

    final var currentUser = appUserClient.get().getOptionalCurrentAppUser();
    if (currentUser.isEmpty()) {
      injector.getInstance(LogoutUtil.class).logout(); // user has changed his own username - log him out to avoid any issues with the security context
      return;
    }

    NotificationFactory.showSuccessNotification(getTranslation("shared.updated.user.0.successfully", bean.getUsername()));
  }
}
