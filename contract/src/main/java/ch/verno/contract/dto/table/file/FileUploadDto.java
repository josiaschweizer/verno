package ch.verno.contract.dto.table.file;

import jakarta.annotation.Nonnull;

public record FileUploadDto(@Nonnull String filename,
                            @Nonnull String contentType,
                            @Nonnull byte[] byteContent,
                            long size) {
}
