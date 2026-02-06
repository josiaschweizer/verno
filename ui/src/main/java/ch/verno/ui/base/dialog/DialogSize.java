package ch.verno.ui.base.dialog;

import jakarta.annotation.Nonnull;

public enum DialogSize {
  BIG("90vh", "min(1500px, 95vw)", "1500px", "320px"),
  MEDIUM("30vh", "min(1500px, 40vw)", "1500px", "320px"),
  SMALL("15vh", "20vw", "1500px", "320px"),
  ;

  @Nonnull private final String height;
  @Nonnull private final String width;
  @Nonnull private final String maxWidth;
  @Nonnull private final String minWidth;

  DialogSize(@Nonnull final String height,
             @Nonnull final String width,
             @Nonnull final String maxWidth,
             @Nonnull final String minWidth) {
    this.height = height;
    this.width = width;
    this.maxWidth = maxWidth;
    this.minWidth = minWidth;
  }

  @Nonnull
  public String getHeight() {
    return height;
  }

  @Nonnull
  public String getWidth() {
    return width;
  }

  @Nonnull
  public String getMaxWidth() {
    return maxWidth;
  }

  @Nonnull
  public String getMinWidth() {
    return minWidth;
  }
}
