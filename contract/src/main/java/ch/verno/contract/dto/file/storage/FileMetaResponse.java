package ch.verno.contract.dto.file.storage;

import jakarta.annotation.Nonnull;

public record FileMetaResponse(
        @Nonnull Long id,
        @Nonnull String filename,
        @Nonnull String contentType,
        long size,
        @Nonnull String checksumSha256
) {
}

