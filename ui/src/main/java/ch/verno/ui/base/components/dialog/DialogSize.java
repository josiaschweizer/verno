package ch.verno.ui.base.components.dialog;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public enum DialogSize {

  BIG(
          "clamp(320px, 95vw, 1500px)",
          "90vh",
          "320px",
          "70vh"
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
  ),
  SMALL_COMPACT(
          "clamp(280px, 35vw, 600px)",
          "60vh",
          "280px"
  );

  @Nonnull private final String width;
  @Nonnull private final String maxHeight;
  @Nonnull private final String minWidth;
  @Nullable private final String minHeight;

  DialogSize(@Nonnull final String width,
             @Nonnull final String maxHeight,
             @Nonnull final String minWidth) {
    this(width, maxHeight, minWidth, null);
  }

  DialogSize(@Nonnull final String width,
             @Nonnull final String maxHeight,
             @Nonnull final String minWidth,
             @Nullable final String minHeight) {
    this.width = width;
    this.maxHeight = maxHeight;
    this.minWidth = minWidth;
    this.minHeight = minHeight;
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

  @Nullable
  public String getMinHeight() {
    return minHeight;
  }
}