package ch.verno.ui.base.dialog;

import jakarta.annotation.Nonnull;

public enum DialogSize {

  BIG(
          "clamp(320px, 95vw, 1500px)",
          "90vh",
          "320px"
  ),
  MEDIUM(
          "clamp(320px, 70vw, 1100px)",
          "80vh",
          "320px"
  ),
  MEDIUM_COMPACT(
          "clamp(320px, 50vw, 900px)",
          "80vh",
          "320px"
  ),
  SMALL(
          "clamp(320px, 45vw, 800px)",
          "70vh",
          "320px"
  );

  @Nonnull private final String width;
  @Nonnull private final String maxHeight;
  @Nonnull private final String minWidth;

  DialogSize(@Nonnull final String width,
             @Nonnull final String maxHeight,
             @Nonnull final String minWidth) {
    this.width = width;
    this.maxHeight = maxHeight;
    this.minWidth = minWidth;
  }

  @Nonnull
  public String getWidth() {
    return width;
  }

  @Nonnull
  public String getMaxHeight() {
    return maxHeight;
  }

  @Nonnull
  public String getMinWidth() {
    return minWidth;
  }
}