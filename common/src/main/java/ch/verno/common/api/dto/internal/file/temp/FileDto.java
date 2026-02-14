package ch.verno.common.api.dto.internal.file.temp;

import jakarta.annotation.Nonnull;

public record FileDto(@Nonnull String filename,
                      @Nonnull byte[] pdfBytes) {
}
