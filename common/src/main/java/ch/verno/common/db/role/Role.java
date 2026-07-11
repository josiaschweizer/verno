package ch.verno.common.db.role;

import ch.verno.common.dto.ui.badge.VABadgeLabelOptions;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Objects;

public enum Role {
  ADMIN(1L, RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_NAME_KEY_ADMIN, VABadgeLabelOptions.ERROR),
  MANDANT_ADMIN(2L, RoleConstants.ROLE_MANDANT_ADMIN, RoleConstants.ROLE_NAME_KEY_MANDANT_ADMIN, VABadgeLabelOptions.WARNING),
  USER(3L, RoleConstants.ROLE_USER, RoleConstants.ROLE_NAME_KEY_USER, VABadgeLabelOptions.SUCCESS),
  VIEWER(4L, RoleConstants.ROLE_VIEWER, RoleConstants.ROLE_NAME_KEY_VIEWER, VABadgeLabelOptions.CONTRAST);

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

  private static class RoleConstants {

    @NonNls public static final String ROLE_ADMIN = "ADMIN";
    @NonNls public static final String ROLE_MANDANT_ADMIN = "MANDANT_ADMIN";
    @NonNls public static final String ROLE_USER = "USER";
    @NonNls public static final String ROLE_VIEWER = "VIEWER";

    @NonNls public static final String ROLE_NAME_KEY_ADMIN = "Admin";
    @NonNls public static final String ROLE_NAME_KEY_MANDANT_ADMIN = "Mandant Admin";
    @NonNls public static final String ROLE_NAME_KEY_USER = "User";
    @NonNls public static final String ROLE_NAME_KEY_VIEWER = "Viewer";

  }
}
