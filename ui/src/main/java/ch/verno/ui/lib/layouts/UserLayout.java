package ch.verno.ui.lib.layouts;

import ch.verno.common.db.role.Role;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.common.ui.dto.UserDtoUnhashedPw;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class UserLayout {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final EntryFactory<UserDtoUnhashedPw> entryFactory;
  @Nonnull private final IAppUserService appUserService;

  @Nonnull private String usernamePanelDisabledReasonKey = Publ.EMPTY_STRING;
  @Nonnull private String roleDisabledReasonKey = Publ.EMPTY_STRING;
  @Nonnull private String passwordDisabledReasonKey = Publ.EMPTY_STRING;

  public UserLayout(@Nonnull final GlobalInterface globalInterface,
                    @Nonnull final EntryFactory<UserDtoUnhashedPw> entryFactory) {
    this.globalInterface = globalInterface;
    this.appUserService = globalInterface.getService(IAppUserService.class);
    this.entryFactory = entryFactory;
  }

  @Nonnull
  public HorizontalLayout buildUserLayout(@Nonnull final Binder<UserDtoUnhashedPw> binder,
                                          @Nonnull final FormMode formMode,
                                          @Nonnull final String oldUserName) {
    final var username = createUserNameField(binder, formMode, oldUserName);

    final var email = entryFactory.createEmailEntry(
            UserDtoUnhashedPw::getEmail,
            UserDtoUnhashedPw::setEmail,
            binder,
            Optional.empty(),
            TranslationHelper.getTranslation(globalInterface, "shared.e.mail")
    );

    final var firstname = entryFactory.createTextEntry(
            UserDtoUnhashedPw::getFirstname,
            UserDtoUnhashedPw::setFirstname,
            binder,
            Optional.empty(),
            TranslationHelper.getTranslation(globalInterface, "shared.first.name")
    );
    final var lastname = entryFactory.createTextEntry(
            UserDtoUnhashedPw::getLastname,
            UserDtoUnhashedPw::setLastname,
            binder,
            Optional.empty(),
            TranslationHelper.getTranslation(globalInterface, "shared.last.name")
    );

    final var password = entryFactory.createPasswordField(
            UserDtoUnhashedPw::getPassword,
            UserDtoUnhashedPw::setPassword,
            binder,
            Optional.of(TranslationHelper.getTranslation(globalInterface, "shared.password.is.required")),
            TranslationHelper.getTranslation(globalInterface, "shared.password")
    );

    final var role = entryFactory.createEnumComboBoxEntry(
            UserDtoUnhashedPw::getRole,
            UserDtoUnhashedPw::setRole,
            binder,
            Arrays.stream(Role.values())
                    .sorted(Comparator.comparing(Role::getId).reversed())
                    .toArray(Role[]::new),
            Optional.of(TranslationHelper.getTranslation(globalInterface, "shared.role.is.required")),
            TranslationHelper.getTranslation(globalInterface, "shared.role"),
            Role::getRoleNameKey
    );


    if (!usernamePanelDisabledReasonKey.isBlank()) {
      username.setReadOnly(true);
      username.setTooltipText(TranslationHelper.getTranslation(globalInterface, usernamePanelDisabledReasonKey));
    }
    if (!roleDisabledReasonKey.isBlank()) {
      role.setReadOnly(true);
      role.setTooltipText(TranslationHelper.getTranslation(globalInterface, roleDisabledReasonKey));
    }
    if (!passwordDisabledReasonKey.isBlank()) {
      password.setReadOnly(true);
      password.setTooltipText(TranslationHelper.getTranslation(globalInterface, passwordDisabledReasonKey));
    }

    return LayoutUtil.createHorizontalLayoutFromComponents(username, email, firstname, lastname, password, role);
  }


  @Nonnull
  private TextField createUserNameField(@Nonnull final Binder<UserDtoUnhashedPw> binder,
                                        @Nonnull final FormMode formMode,
                                        @Nonnull final String oldUserName) {
    final var username = new TextField(TranslationHelper.getTranslation(globalInterface, "shared.username"));
    username.setWidthFull();
    username.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

    final var usernameBinding = binder.forField(username)
            .asRequired(TranslationHelper.getTranslation(globalInterface, "shared.username.is.required"))
            .withValidator((value, context) -> {
              if (value == null || value.isBlank()) {
                return ValidationResult.error(TranslationHelper.getTranslation(globalInterface, "shared.username.is.required"));
              }
              final var userNameExists = appUserService.findByUserName(value);
              if (userNameExists.isEmpty()) {
                return ValidationResult.ok();
              } else if (formMode == FormMode.EDIT && value.equals(oldUserName)) {
                return ValidationResult.ok(); // allow unchanged username in edit mode
              } else {
                return ValidationResult.error(TranslationHelper.getTranslation(globalInterface, "shared.username.0.already.exists", value));
              }
            });
    usernameBinding.bind(UserDtoUnhashedPw::getUsername, UserDtoUnhashedPw::setUsername);
    return username;
  }

  public void setUsernamePanelReadOnly(@Nonnull final String disabledReasonKey) {
    this.usernamePanelDisabledReasonKey = disabledReasonKey;
  }

  public void setRoleReadOnly(@Nonnull final String disabledReasonKey) {
    this.roleDisabledReasonKey = disabledReasonKey;
  }

  public void setPasswordReadOnly(@Nonnull final String disabledReasonKey) {
    this.passwordDisabledReasonKey = disabledReasonKey;
  }

}
