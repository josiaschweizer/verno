package ch.verno.common.report;

import jakarta.annotation.Nonnull;

public record ReportDto(@Nonnull String filename,
                        @Nonnull byte[] pdfBytes) {
}
