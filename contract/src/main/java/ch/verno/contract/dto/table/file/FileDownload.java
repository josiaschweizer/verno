package ch.verno.contract.dto.table.file;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.InputStream;

public record FileDownload(
        @Nonnull StoredFileDto meta,
        @Nullable InputStream stream
) {
}

