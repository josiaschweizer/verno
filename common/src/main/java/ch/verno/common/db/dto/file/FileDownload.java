package ch.verno.common.db.dto.file;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.InputStream;

public record FileDownload(
        @Nonnull StoredFile meta,
        @Nullable InputStream stream
) {
}

