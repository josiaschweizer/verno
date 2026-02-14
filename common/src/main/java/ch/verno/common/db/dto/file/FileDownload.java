package ch.verno.common.db.dto.file;

import jakarta.annotation.Nonnull;

import java.io.InputStream;

public record FileDownload(
        @Nonnull StoredFile meta,
        @Nonnull InputStream stream
) {
}

