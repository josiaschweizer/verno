package ch.verno.common.db.dto.file;

import jakarta.annotation.Nonnull;

public record StoredFile(
        @Nonnull Long id,
        @Nonnull String filename,
        @Nonnull String contentType,
        long size,
        @Nonnull String checksumSha256
) {
}
