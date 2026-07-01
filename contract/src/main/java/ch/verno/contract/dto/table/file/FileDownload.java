package ch.verno.contract.dto.table.file;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record FileDownload(
        @Nonnull StoredFileDto meta,
        @Nullable byte[] byteData
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

