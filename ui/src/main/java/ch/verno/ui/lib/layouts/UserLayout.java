package ch.verno.ui.lib.layouts;

import ch.verno.common.db.role.Role;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.user.AppUserClient;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.i18n.TranslationHelper;
import ch.verno.ui.lib.util.LayoutUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

public class UserLayout {

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<AppUserClient> appUserClient;
  @Nonnull private final TranslationHelper translationHelper;

  @Nonnull private final EntryFactory<UserDtoUnhashedPw> entryFactory;

  @Nonnull private String usernamePanelDisabledReasonKey = Publ.EMPTY_STRING;
  @Nonnull private String roleDisabledReasonKey = Publ.EMPTY_STRING;
  @Nonnull private String passwordDisabledReasonKey = Publ.EMPTY_STRING;

  @Inject
  public UserLayout(@Nonnull final Injector injector) {
    this.injector = injector;
    this.appUserClient = Lazy.of(() -> injector.getInstance(AppUserClient.class));
    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));
    this.translationHelper = injector.getInstance(TranslationHelper.class);
  }

  @Nonnull
  public VAHorizontalLayout buildUserLayout(@Nonnull final Binder<UserDtoUnhashedPw> binder,
                                            @Nonnull final FormMode formMode,
                                            @Nonnull final String oldUserName) {
    final var username = createUserNameField(binder, formMode, oldUserName);

    final var email = entryFactory.createEmailEntry(
            UserDtoUnhashedPw::getEmail,
            UserDtoUnhashedPw::setEmail,
            binder,
            Optional.empty(),
            translationHelper.getTranslation("shared.e.mail")
    );

    final var firstname = entryFactory.createTextField(
            UserDtoUnhashedPw::getFirstname,
            UserDtoUnhashedPw::setFirstname,
            binder,
            Optional.empty(),
            translationHelper.getTranslation( "shared.first.name")
    );
    final var lastname = entryFactory.createTextField(
            UserDtoUnhashedPw::getLastname,
            UserDtoUnhashedPw::setLastname,
            binder,
            Optional.empty(),
            translationHelper.getTranslation( "shared.last.name")
    );

    final var password = entryFactory.createPasswordField(
            UserDtoUnhashedPw::getPassword,
            UserDtoUnhashedPw::setPassword,
            binder,
            Optional.of(translationHelper.getTranslation( "shared.password.is.required")),
            translationHelper.getTranslation( "shared.password")
    );

    final var role = entryFactory.createEnumComboBoxEntry(
            UserDtoUnhashedPw::getRole,
            UserDtoUnhashedPw::setRole,
            binder,
            Arrays.stream(Role.values())
                    .sorted(Comparator.comparing(Role::getId).reversed())
                    .toArray(Role[]::new),
            Optional.of(translationHelper.getTranslation( "shared.role.is.required")),
            translationHelper.getTranslation( "shared.role"),
            Role::getRoleNameKey
    );


    if (!usernamePanelDisabledReasonKey.isBlank()) {
      username.setReadOnly(true);
      username.setTooltipText(translationHelper.getTranslation( usernamePanelDisabledReasonKey));
    }
    if (!roleDisabledReasonKey.isBlank()) {
      role.setReadOnly(true);
      role.setTooltipText(translationHelper.getTranslation( roleDisabledReasonKey));
    }
    if (!passwordDisabledReasonKey.isBlank()) {
      password.setReadOnly(true);
      password.setRevealButtonVisible(false); // hide the reveal button so the user cannot see the password (used e.g. in edit user when the password is just the hash of the pw...)
      password.setTooltipText(translationHelper.getTranslation( passwordDisabledReasonKey));
    }

    return LayoutUtil.createHorizontal(username, email, firstname, lastname, password, role);
  }


  @Nonnull
  private TextField createUserNameField(@Nonnull final Binder<UserDtoUnhashedPw> binder,
                                        @Nonnull final FormMode formMode,
                                        @Nonnull final String oldUserName) {
    final var username = new TextField(translationHelper.getTranslation( "shared.username"));
    username.setWidthFull();
    username.setValueChangeMode(com.vaadin.flow.data.value.ValueChangeMode.EAGER);

    final var usernameBinding = binder.forField(username)
            .asRequired(translationHelper.getTranslation( "shared.username.is.required"))
            .withValidator((value, context) -> {
              if (value == null || value.isBlank()) {
                return ValidationResult.error(translationHelper.getTranslation( "shared.username.is.required"));
              }
              final var userNameExists = appUserClient.get().findByUsername(value);
              if (userNameExists.isEmpty()) {
                return ValidationResult.ok();
              } else if (formMode == FormMode.EDIT && value.equals(oldUserName)) {
                return ValidationResult.ok(); // allow unchanged username in edit mode
              } else {
                return ValidationResult.error(translationHelper.getTranslation( "shared.username.0.already.exists", value));
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
