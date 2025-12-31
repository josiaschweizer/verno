package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AppUserDto extends BaseDto {
  @Nonnull
  private String username;

  @Nonnull
  private String role;

  protected AppUserDto() {
    // JPA
  }

  public AppUserDto(@Nonnull final String username,
                    @Nonnull final String role) {
    this(null, username, role);
  }

  public AppUserDto(@Nullable final Long id,
                    @Nonnull final String username,
                    @Nonnull final String role) {
    setId(id);
    this.username = username;
    this.role = role;
  }

  @Nonnull
  public String getUsername() {
    return username;
  }

  public void setUsername(@Nonnull final String username) {
    this.username = username;
  }

  @Nonnull
  public String getRole() {
    return role;
  }

  public void setRole(@Nonnull final String role) {
    this.role = role;
  }
}
