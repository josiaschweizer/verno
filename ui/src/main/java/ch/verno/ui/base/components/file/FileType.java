package ch.verno.ui.base.components.file;

import jakarta.annotation.Nonnull;

public enum FileType {
  PDF("application/pdf"),
  CSV("text/csv"),
  HTML("text/html"),
  ;

  @Nonnull private final String mimeType;

  FileType(@Nonnull final String mimeType) {
    this.mimeType = mimeType;
  }

  @Nonnull
  public String getMimeType() {
    return mimeType;
  }

}
