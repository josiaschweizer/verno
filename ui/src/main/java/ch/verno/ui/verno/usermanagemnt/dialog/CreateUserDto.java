package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.role.Role;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class CreateUserDto {

  @Nonnull private String username;
  @Nonnull private String email;
  @Nonnull private String firstname;
  @Nonnull private String lastname;
  @Nonnull private String password;
  @Nonnull private Role role;
  private boolean active;

  public CreateUserDto() {
    username = Publ.EMPTY_STRING;
    email = Publ.EMPTY_STRING;
    firstname = Publ.EMPTY_STRING;
    lastname = Publ.EMPTY_STRING;
    password = Publ.EMPTY_STRING;
    role = Role.USER;
    active = false;
  }

  @Nonnull
  public static CreateUserDto fromAppUserDto(@Nonnull final AppUserDto appUserDto) {
    final CreateUserDto dto = new CreateUserDto();
    dto.setUsername(appUserDto.getUsername());
    dto.setEmail(appUserDto.getEmail());
    dto.setFirstname(appUserDto.getFirstname());
    dto.setLastname(appUserDto.getLastname());
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
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(@Nonnull final String firstname) {
    this.firstname = firstname;
  }

  @Nonnull
  public String getLastname() {
    return lastname;
  }

  public void setLastname(@Nonnull final String lastname) {
    this.lastname = lastname;
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
