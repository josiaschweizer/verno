package ch.verno.common.db.role;

import ch.verno.common.base.components.badge.VABadgeLabelOptions;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public enum Role {
  ADMIN(1L, "ADMIN", "Admin", VABadgeLabelOptions.ERROR),
  MANDANT_ADMIN(2L, "MANDANT_ADMIN", "Mandant Admin", VABadgeLabelOptions.WARNING),
  USER(3L, "USER", "User", VABadgeLabelOptions.SUCCESS),
  VIEWER(4L, "VIEWER", "Viewer", VABadgeLabelOptions.CONTRAST);

  private final Long id;
  @Nonnull private final String role;
  @Nonnull private final String roleNameKey;
  @Nonnull private final VABadgeLabelOptions badgeLabelOptions;

  Role(final Long id,
       @Nonnull final String role,
       @Nonnull final String roleNameKey,
       @Nonnull final VABadgeLabelOptions badgeLabelOptions) {
    this.id = id;
    this.role = role;
    this.roleNameKey = roleNameKey;
    this.badgeLabelOptions = badgeLabelOptions;
  }

  @Nonnull
  public static Role fromId(final Long id) {
    for (final var role : values()) {
      if (Objects.equals(role.getId(), id)) {
        return role;
      }
    }

    throw new IllegalArgumentException("No enum constant " + Role.class.getCanonicalName() + " with id " + id);
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

  public Long getId() {
    return id;
  }

  @Nonnull
  public String getRole() {
    return role;
  }

  @Nonnull
  public String getRoleNameKey() {
    return roleNameKey;
  }

  @Nonnull
  public VABadgeLabelOptions getBadgeLabelOptions() {
    return badgeLabelOptions;
  }
}
