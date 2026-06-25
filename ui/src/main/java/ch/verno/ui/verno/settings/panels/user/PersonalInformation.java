package ch.verno.ui.verno.settings.panels.user;

import ch.verno.common.db.role.Role;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.layouts.UserLayout;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.verno.usermanagemnt.dialog.ChangePasswordDialog;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;

public class PersonalInformation extends VABaseSetting<UserDtoUnhashedPw> {

  public static final String TITLE_KEY = "shared.my.profile";

  @Nonnull private final Lazy<AppUserClient> userClient;

  @Inject
  public PersonalInformation(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);
    this.userClient = Lazy.of(() -> injector.getInstance(AppUserClient.class));
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

    menu.addItem(getTranslation("shared.passwort.andern"), e -> {
      final var userId = dto.getId() != null ? dto.getId() : -1;
      new ChangePasswordDialog(injector, userId).open();
    });

    return button;
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var userLayout = new UserLayout(globalInterface, new EntryFactory<>(globalInterface.getI18NProvider()));
    userLayout.setUsernamePanelReadOnly(getTranslation("shared.der.benutzername.kann.nur.durch.den.administrator.geandert.werden"));

    final var currentUser = globalInterface.getUserProperties().getCurrentUser();
    if (currentUser.getRole().equals(Role.ADMIN)) {
      userLayout.setRoleReadOnly(getTranslation("shared.ein.administrator.kann.sich.selber.nicht.die.rolle.entziehen.bitte.wenden.sie.sich.an.einen.anderen.administrator.um.ihre.rolle.zu.andern"));
    } else {
      userLayout.setRoleReadOnly(getTranslation("shared.die.rolle.kann.nur.durch.den.administrator.geandert.werden"));
    }

    userLayout.setPasswordReadOnly(getTranslation("shared.das.passwort.kann.nur.uber.den.passwort.andern.dialog.geandert.werden.erreichbar.uber.das.drei.punkt.menu"));
    return userLayout.buildUserLayout(binder, FormMode.EDIT, dto.getUsername());
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      final var updatedUser = new AppUserDto(
              dto.getId(),
              dto.getUsername(),
              dto.getFirstname(),
              dto.getLastname(),
              dto.getEmail(),
              Publ.EMPTY_STRING, // password cannot be updated here -> set to empty string
              dto.getRole(),
              dto.isActive()
      );

      final var updated = userService.updateAppUser(updatedUser);
      dto = UserDtoUnhashedPw.fromAppUserDto(updated);
      dto.setPassword(Publ.EIGHT_STARS);

      binder.readBean(dto);
      saveButton.setEnabled(false);
    }
  }

  @Nonnull
  @Override
  protected Class<UserDtoUnhashedPw> getBeanType() {
    return UserDtoUnhashedPw.class;
  }

  @Nonnull
  @Override
  protected UserDtoUnhashedPw createNewBeanInstance() {
    final var currentUser = globalInterface.getUserProperties().getCurrentUser();
    currentUser.setPasswordHash(Publ.EIGHT_STARS);
    return UserDtoUnhashedPw.fromAppUserDto(currentUser);
  }
}
