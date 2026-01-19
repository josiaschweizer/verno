package ch.verno.common.file.dto;

import jakarta.annotation.Nonnull;

public record FileDto(@Nonnull String filename,
                      @Nonnull byte[] pdfBytes) {
}
