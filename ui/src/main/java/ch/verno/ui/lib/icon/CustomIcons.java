package ch.verno.ui.lib.icon;

import jakarta.annotation.Nonnull;

public enum CustomIcons {
  VERNO_LOGO(CustomIconConstants.VERNO_LOGO, "icons/verno.svg"),
  USER_COG(CustomIconConstants.USER_COG, "icons/user-cog.svg");

  @Nonnull private final String name;
  @Nonnull private final String path;

  CustomIcons(@Nonnull final String name,
              @Nonnull final String path) {
    this.name = name;
    this.path = path;
  }

  @Nonnull
  public static CustomIcons of(@Nonnull final String name) {
    for (final CustomIcons icon : values()) {
      if (icon.getName().equals(name)) {
        return icon;
      }
    }

    throw new IllegalArgumentException("No CustomIcon found with name: " + name);
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public String getPath() {
    return path;
  }

}