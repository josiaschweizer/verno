package ch.verno.contract.dto.file.temp;

import jakarta.annotation.Nonnull;

public record FileDto(@Nonnull String filename,
                      @Nonnull byte[] pdfBytes) {
}
