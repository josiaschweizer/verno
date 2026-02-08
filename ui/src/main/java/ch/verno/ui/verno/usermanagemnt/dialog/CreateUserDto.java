package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class CreateUserDto {

  @Nonnull private String username;
  @Nonnull private String password;
  @Nonnull private Role role;
  private boolean active;

  public CreateUserDto() {
    username = Publ.EMPTY_STRING;
    password = Publ.EMPTY_STRING;
    role = Role.USER;
    active = false;
  }

  @Nonnull
  public static CreateUserDto fromAppUserDto(@Nonnull final AppUserDto appUserDto) {
    final CreateUserDto dto = new CreateUserDto();
    dto.setUsername(appUserDto.getUsername());
    dto.setPassword(appUserDto.getPasswordHash());
    dto.setRole(appUserDto.getRole());
    dto.setActive(appUserDto.isActive());
    return dto;
  }

  @Nonnull
  public String getUsername() {
    return username;
  }

  public void setUsername(@Nonnull final String username) {
    this.username = username;
  }

  @Nonnull
  public String getPassword() {
    return password;
  }

  public void setPassword(@Nonnull final String password) {
    this.password = password;
  }

  @Nonnull
  public Role getRole() {
    return role;
  }

  public void setRole(@Nonnull final Role role) {
    this.role = role;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }
}
