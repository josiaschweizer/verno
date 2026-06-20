package ch.verno.ui.base.components.file;

import jakarta.annotation.Nonnull;

public enum FileType {
  PDF("application/pdf", ".pdf"),
  CSV("text/csv", ".csv"),
  HTML("text/html", ".html"),
  ;

  @Nonnull private final String mimeType;
  @Nonnull private final String fileExtension;

  FileType(@Nonnull final String mimeType,
           @Nonnull final String fileExtension) {
    this.mimeType = mimeType;
    this.fileExtension = fileExtension;
  }

  @Nonnull
  public String getMimeType() {
    return mimeType;
  }

  @Nonnull
  public String getFileExtension() {
    return fileExtension;
  }
}
