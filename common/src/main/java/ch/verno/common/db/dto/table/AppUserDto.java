package ch.verno.common.db.dto.table;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.role.Role;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AppUserDto extends BaseDto {

  @Nonnull private String username;
  @Nonnull private String firstname;
  @Nonnull private String lastname;
  @Nonnull private String email;
  @Nonnull private String passwordHash;
  @Nonnull private Role role;
  private boolean active;

  public AppUserDto() {
    username = Publ.EMPTY_STRING;
    passwordHash = Publ.EMPTY_STRING;
    role = Role.USER;
    active = false;
  }

  public AppUserDto(@Nonnull final String username,
                    @Nonnull final String firstname,
                    @Nonnull final String lastname,
                    @Nonnull final String email,
                    @Nonnull final String passwordHash,
                    @Nonnull final Role role,
                    final boolean active) {
    this(null, username, firstname, lastname, email, passwordHash, role, active);
  }

  public AppUserDto(@Nullable final Long id,
                    @Nonnull final String username,
                    @Nonnull final String firstname,
                    @Nonnull final String lastname,
                    @Nonnull final String email,
                    @Nonnull final String passwordHash,
                    @Nonnull final Role role,
                    final boolean active) {
    setId(id);
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.passwordHash = passwordHash;
    this.role = role;
    this.active = active;
  }

  @Nonnull
  public String getUsername() {
    return username;
  }

  public void setUsername(@Nonnull final String username) {
    this.username = username;
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
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(@Nonnull final String passwordHash) {
    this.passwordHash = passwordHash;
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
