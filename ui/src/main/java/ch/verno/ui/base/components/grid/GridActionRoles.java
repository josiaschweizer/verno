package ch.verno.ui.base.components.grid;

import jakarta.annotation.Nonnull;

public enum GridActionRoles {
  STICK_COLUMN("stickColumn"),
  INVISIBLE_COLUMN("invisibleColumn")
  ;

  @Nonnull private final String action;

  GridActionRoles(@Nonnull final String action) {
    this.action = action;
  }

  @Nonnull
  public String getAction() {
    return action;
  }
}
