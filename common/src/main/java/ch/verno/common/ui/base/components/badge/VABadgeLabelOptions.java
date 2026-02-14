package ch.verno.common.ui.base.components.badge;

import jakarta.annotation.Nonnull;

public enum VABadgeLabelOptions {
  SUCCESS("badge success"),
  WARNING("badge warning"),
  ERROR("badge error"),
  CONTRAST("badge contrast"),
  NORMAL("badge"),
  ;

  @Nonnull
  private final String theme;

  VABadgeLabelOptions(@Nonnull final String theme) {
    this.theme = theme;
  }

  @Nonnull
  public String getTheme() {
    return theme;
  }
}
