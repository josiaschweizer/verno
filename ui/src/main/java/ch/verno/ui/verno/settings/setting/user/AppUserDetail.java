package ch.verno.ui.verno.settings.setting.user;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.common.db.service.IAppUserService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.publ.Routes;
import ch.verno.ui.base.settings.grid.BaseSettingDetail;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@UIScope
@SpringComponent
public class AppUserDetail extends BaseSettingDetail<AppUserDto> {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final IAppUserService appUserService;

  public AppUserDetail(@Nonnull final GlobalInterface globalInterface) {
    this.passwordEncoder = globalInterface.getPasswordEncoder();
    this.appUserService = globalInterface.getService(IAppUserService.class);
    init();
  }

  @Override
  protected void initUI() {
    final var usernameEntry = entryFactory.createTextEntry(
            AppUserDto::getUsername,
            AppUserDto::setUsername,
            getBinder(),
            Optional.of(getTranslation(getTranslation("shared.username.is.required"))),
            getTranslation(getTranslation("shared.username"))
    );

    final var passwordEntry = createPasswordEntry();
    final var roleEntry = createRoleEntry();
    final var activeEntry = createActiveEntry();

    add(createLayoutFromComponents(usernameEntry, passwordEntry, roleEntry, activeEntry));
  }

  @Nonnull
  private PasswordField createPasswordEntry() {
    return entryFactory.createPasswordField(
            AppUserDto::getPasswordHash,
            (appUserDto, rawPassword) -> appUserDto.setPasswordHash(getHashedPassword(rawPassword)),
            getBinder(),
            Optional.of(getTranslation("shared.password.is.required")),
            getTranslation("shared.password"),
            false //todo new mode correct set
    );
  }

  @Nonnull
  private ComboBox<Role> createRoleEntry() {
    final var roleComboBox = new ComboBox<Role>(getTranslation("shared.role"));
    roleComboBox.setWidthFull();
    roleComboBox.setItems(getAvailableRoles());
    roleComboBox.setItemLabelGenerator(role -> getTranslation(role.getRoleNameKey()));
    roleComboBox.setRequired(true);

    getBinder().forField(roleComboBox)
            .asRequired(getTranslation("shared.role.is.required"))
            .bind(AppUserDto::getRole, AppUserDto::setRole);

    return roleComboBox;
  }

  @Nonnull
  private Checkbox createActiveEntry() {
    final var activeCheckbox = new Checkbox(getTranslation("shared.active"));
    getBinder().forField(activeCheckbox).bind(AppUserDto::isActive, AppUserDto::setActive);
    return activeCheckbox;
  }

  @Nonnull
  private List<Role> getAvailableRoles() {
    return Arrays.stream(Role.values()).toList();
  }

  @Nonnull
  @Override
  protected String getDetailPageName() {
    return getTranslation(getTranslation("shared.application.user"));
  }

  @Nonnull
  @Override
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(Routes.APP_USERS, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected String getBasePageRoute() {
    return Routes.APP_USERS;
  }

  @Nonnull
  @Override
  protected Binder<AppUserDto> createBinder() {
    return new Binder<>(AppUserDto.class);
  }

  @Nonnull
  @Override
  protected AppUserDto createBean(@Nonnull final AppUserDto bean) {
    return appUserService.createAppUser(bean);
  }

  @Nonnull
  @Override
  protected AppUserDto updateBean(@Nonnull final AppUserDto bean) {
    return appUserService.updateAppUser(bean);
  }

  @Nonnull
  @Override
  protected AppUserDto newBeanInstance() {
    final var appUser = new AppUserDto();
    appUser.setActive(true);
    return appUser;
  }

  @Nonnull
  @Override
  protected AppUserDto getBeanById(@Nonnull final Long id) {
    return appUserService.findAppUserById(id);
  }

  @Nonnull
  private String getHashedPassword(@Nonnull final String rawPassword) {
    final var hash = passwordEncoder.encode(rawPassword);
    return hash != null ? hash : Publ.EMPTY_STRING;
  }
}