package ch.verno.contract.dto.table.file;

import jakarta.annotation.Nonnull;

import java.io.InputStream;

public record FileUploadDto(@Nonnull String filename,
                            @Nonnull String contentType,
                            @Nonnull InputStream content,
                            long size) {
}
