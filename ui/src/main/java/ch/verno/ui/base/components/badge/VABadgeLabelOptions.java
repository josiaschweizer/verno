package ch.verno.ui.base.components.badge;

import jakarta.annotation.Nonnull;

public enum VABadgeLabelOptions {
  SUCCESS("badge success"),
  WARNING("badge warning"),
  ERROR("badge error"),
  CONTRAST("badge contrast"),
  NORMAL("badge"),
  ;

  @Nonnull
  private final String className;

  VABadgeLabelOptions(@Nonnull final String className) {
    this.className = className;
  }

  @Nonnull
  public String getClassName() {
    return className;
  }
}
