package ch.verno.contract.dto.table.file;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.InputStream;

public record FileDownload(
        @Nonnull StoredFileDto meta,
        @Nullable InputStream stream
) {

  @Nonnull
  public static FileDownload empty() {
    return new FileDownload(StoredFileDto.empty(), null);
  }

  @Nonnull
  public static FileDownload noContent(@Nonnull final StoredFileDto meta) {
    return new FileDownload(meta, null);
  }

}

