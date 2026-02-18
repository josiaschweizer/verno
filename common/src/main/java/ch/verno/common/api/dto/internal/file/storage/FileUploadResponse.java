package ch.verno.common.api.dto.internal.file.storage;

import jakarta.annotation.Nonnull;

public record FileUploadResponse(
        @Nonnull Long id,
        @Nonnull String filename,
        @Nonnull String contentType,
        long size
) {
}
