package ch.verno.server.file.temp;


import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.Normalizer;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TempFileHandler {

  @Nonnull private final Path baseDir;
  @Nonnull private final Map<String, TempFileEntry> index;

  public TempFileHandler() {
    index = new ConcurrentHashMap<>();

    try {
      this.baseDir = Files.createTempDirectory(VernoConstants.TEMP_FILE_BASE_DIR);
    } catch (IOException e) {
      throw new IllegalStateException("Could not create temp directory", e);
    }
  }

  @Nonnull
  public String store(@Nonnull final String filename,
                      @Nonnull final byte[] data) {
    final String token = UUID.randomUUID().toString();
    final String safeName = sanitizeFilename(filename);

    final Path file = baseDir.resolve(token + Publ.MINUS + safeName);

    try {
      Files.write(file, data, StandardOpenOption.CREATE_NEW);
    } catch (IOException e) {
      throw new RuntimeException("Failed to write temp file: " + file, e);
    }

    index.put(token, new TempFileEntry(safeName, file, Instant.now().plusSeconds(10600)));
    return token;
  }

  @Nonnull
  public FileDto load(@Nonnull final String token) {
    final TempFileEntry entry = resolveEntry(token);

    if (Instant.now().isAfter(entry.expiresAt())) {
      delete(token);
      throw new IllegalArgumentException("Temp file token has expired: " + token);
    }

    try {
      final var pdfBytes = Files.readAllBytes(entry.path());
      return new FileDto(entry.filename(), pdfBytes);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read temp file: " + entry.path(), e);
    }
  }

  @Nonnull
  public TempFileEntry resolveEntry(@Nonnull final String token) {
    return requireEntry(token);
  }

  public void delete(@Nonnull final String token) {
    final TempFileEntry entry = index.remove(token);
    if (entry == null) {
      return;
    }
    try {
      Files.deleteIfExists(entry.path());
    } catch (IOException ignored) {
      // Temp-Cleanup soll nichts blockieren
    }
  }

  @Nonnull
  private TempFileEntry requireEntry(@Nonnull final String token) {
    final TempFileEntry entry = index.get(token);
    if (entry == null) {
      throw new IllegalArgumentException("Temp file not found for token: " + token);
    }
    if (!Files.exists(entry.path())) {
      index.remove(token);
      throw new IllegalArgumentException("Temp file missing on disk for token: " + token);
    }
    return entry;
  }

  @Nonnull
  private static String sanitizeFilename(@Nonnull final String filename) {
    final var name = filename.replace("\\", "/");
    final var base = name.substring(name.lastIndexOf('/') + 1);

    String normalized = Normalizer.normalize(base, Normalizer.Form.NFD);

    normalized = normalized.replaceAll("\\p{M}", "");

    normalized = normalized
            .toLowerCase(Locale.ROOT)
            .replaceAll("\\s+", "_")
            .replaceAll("[^a-z0-9._-]", "_")
            .replaceAll("_+", "_")
            .replaceAll("^_|_$", "");

    return normalized.isBlank() ? "file" : normalized;
  }
}