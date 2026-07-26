package ch.verno.common.dto.ui.badge;

import jakarta.annotation.Nonnull;

public enum VABadgeLabelOptions {
  SUCCESS(BadgeLabelVariants.SUCCESS),
  WARNING(BadgeLabelVariants.WARNING),
  ERROR(BadgeLabelVariants.ERROR),
  CONTRAST(BadgeLabelVariants.CONTRAST),
  NORMAL(BadgeLabelVariants.NORMAL),
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
