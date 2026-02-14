package ch.verno.ui.verno.settings.setting.user;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.ui.dto.UserDtoUnhashedPw;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import ch.verno.ui.lib.layouts.UserLayout;
import com.vaadin.flow.component.Component;
import jakarta.annotation.Nonnull;

public class PersonalInformation extends VABaseSetting<UserDtoUnhashedPw> {

  @Nonnull private final IAppUserService userService;

  public PersonalInformation(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, "My Profile", true);
    this.userService = globalInterface.getService(IAppUserService.class);
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var userLayout = new UserLayout(globalInterface, new EntryFactory<>(globalInterface.getI18NProvider()));
    userLayout.setUsernamePanelReadOnly("Der Benutzername kann nur durch den Administrator geändert werden.");

    final var currentUser = globalInterface.getCurrentUser();
    if (currentUser.getRole().equals(Role.ADMIN)) {
      userLayout.setRoleReadOnly("Ein Administrator kann sich selber nicht die Rolle entziehen. Bitte wenden Sie sich an einen anderen Administrator, um Ihre Rolle zu ändern.");
    } else {
      userLayout.setRoleReadOnly("Die Rolle kann nur durch den Administrator geändert werden.");
    }

    userLayout.setPasswordReadOnly("Das Passwort kann nur über den 'Passwort ändern'-Dialog geändert werden, erreichbar über das Drei-Punkt-Menü.");
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
      dto.setPassword("********");

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
    final var currentUser = globalInterface.getCurrentUser();
    currentUser.setPasswordHash("*********");
    return UserDtoUnhashedPw.fromAppUserDto(currentUser);
  }
}
