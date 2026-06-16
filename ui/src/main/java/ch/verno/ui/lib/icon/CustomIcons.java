package ch.verno.ui.lib.icon;

import jakarta.annotation.Nonnull;

@SuppressWarnings({"HardcodedFileSeparator", "HardCodedStringLiteral"})
public enum CustomIcons {
  USER_COG(CustomIconConstants.USER_COG, "icons/user-cog.svg"),
  SAVE(CustomIconConstants.SAVE, "icons/save.svg"),
  SAVE_FILLED(CustomIconConstants.SAVE_FILLED, "icons/save_filled.svg"),
  SEND_MAIL(CustomIconConstants.SEND_MAIL, "icons/send-mail.svg")
  ;

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