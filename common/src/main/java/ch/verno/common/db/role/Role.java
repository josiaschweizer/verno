package ch.verno.common.db.role;

import jakarta.annotation.Nonnull;

public enum Role {
  ADMIN("ADMIN", "Admin"),
  USER("USER", "User");

  @Nonnull private final String role;
  @Nonnull private final String roleNameKey;

  Role(@Nonnull final String role,
       @Nonnull final String roleNameKey) {
    this.role = role;
    this.roleNameKey = roleNameKey;
  }

  @Nonnull
  public static Role fromString(@Nonnull final String value) {
    for (final var role : values()) {
      if (role.getRole().equalsIgnoreCase(value)) {
        return role;
      }
    }

    throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + "." + value);
  }

  @Nonnull
  public String getRole() {
    return role;
  }

  @Nonnull
  public String getRoleNameKey() {
    return roleNameKey;
  }
}
