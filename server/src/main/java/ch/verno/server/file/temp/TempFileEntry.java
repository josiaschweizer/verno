package ch.verno.server.file.temp;

import jakarta.annotation.Nonnull;

import java.nio.file.Path;
import java.time.Instant;

public record TempFileEntry(@Nonnull String filename,
                            @Nonnull Path path,
                            @Nonnull Instant expiresAt) {
}