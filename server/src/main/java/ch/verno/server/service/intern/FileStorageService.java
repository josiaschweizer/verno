package ch.verno.server.service.intern;

import ch.verno.common.db.dto.file.FileDownload;
import ch.verno.common.db.dto.file.StoredFile;
import ch.verno.common.db.service.IFileStorageService;
import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.db.storage.ObjectStorage;
import ch.verno.publ.Publ;
import ch.verno.server.repository.file.StoredFileRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class FileStorageService implements IFileStorageService {

  @Nonnull private final StoredFileRepository storedFileRepository;
  @Nonnull private final ObjectStorage objectStorage;

  public FileStorageService(@Nonnull final StoredFileRepository storedFileRepository,
                            @Nonnull final ObjectStorage objectStorage) {
    this.storedFileRepository = storedFileRepository;
    this.objectStorage = objectStorage;
  }

  @Nonnull
  @Override
  @Transactional
  public StoredFile upload(@Nonnull final MultipartFile file) {
    if (file.isEmpty()) {
      throw new IllegalArgumentException("file is empty");
    }

    final var originalFilename = file.getOriginalFilename();
    final var filename = safeFilename(originalFilename != null ? originalFilename : Publ.EMPTY_STRING);
    final var contentType = (file.getContentType() == null || file.getContentType().isBlank())
            ? "application/octet-stream"
            : file.getContentType();

    try (final var inputStream = file.getInputStream()) {
      final var data = inputStream.readAllBytes();
      final var sha256 = sha256Hex(data);

      StoredFileEntity entity = new StoredFileEntity(
              null,
              filename,
              contentType,
              data.length,
              sha256,
              null,
              Instant.now()
      );

      entity = storedFileRepository.save(entity);

      try {
        final var storageKey = buildStorageKey(entity.getId(), filename);
        objectStorage.put(storageKey, new java.io.ByteArrayInputStream(data), data.length);

        entity.setStorageKey(storageKey);
        storedFileRepository.save(entity);
      } catch (Exception storageException) {
        storedFileRepository.delete(entity);
        throw new RuntimeException("Failed to store file in object storage", storageException);
      }

      return new StoredFile(
              entity.getId(),
              entity.getFilename(),
              entity.getContentType(),
              entity.getSize(),
              entity.getChecksumSha256()
      );
    } catch (Exception e) {
      throw new RuntimeException("Upload failed", e);
    }
  }

  @Nonnull
  @Override
  public FileDownload download(@Nonnull final Long id) {
    final var entity = storedFileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found: " + id));

    try {
      final var stream = objectStorage.get(entity.getStorageKey());
      final var meta = new StoredFile(
              entity.getId(),
              entity.getFilename(),
              entity.getContentType(),
              entity.getSize(),
              entity.getChecksumSha256()
      );

      return new FileDownload(meta, stream.orElse(null));
    } catch (Exception e) {
      throw new RuntimeException("Download failed", e);
    }
  }

  @Nonnull
  @Override
  public StoredFile getMeta(@Nonnull final Long id) {
    StoredFileEntity entity = storedFileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found: " + id));

    return new StoredFile(
            entity.getId(),
            entity.getFilename(),
            entity.getContentType(),
            entity.getSize(),
            entity.getChecksumSha256()
    );
  }

  @Override
  @Transactional
  public void delete(@Nonnull final Long id) {
    StoredFileEntity entity = storedFileRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("File not found: " + id));

    try {
      objectStorage.delete(entity.getStorageKey());
      storedFileRepository.delete(entity);
    } catch (Exception e) {
      throw new RuntimeException("Delete failed", e);
    }
  }

  @Nonnull
  private static String buildStorageKey(@Nonnull final Long id,
                                        @Nonnull final String filename) {
    final var stringId = id.toString();
    final var prefix = stringId.length() >= 2 ? stringId.substring(0, 2) : ("0" + stringId);
    return prefix + Publ.SLASH + stringId + Publ.SLASH + filename;
  }

  @Nonnull
  private static String safeFilename(@Nonnull final String original) {
    if (original.isBlank()) {
      return "file.bin";
    }

    String cleaned = original.replace("\\", Publ.SLASH);
    cleaned = cleaned.substring(cleaned.lastIndexOf('/') + 1);
    cleaned = cleaned.replaceAll("[\\r\\n\\t]", "_").trim();

    if (cleaned.isBlank()) {
      return "file.bin";
    }

    return cleaned;
  }

  private static String sha256Hex(byte[] data) {
    try {
      final var md = MessageDigest.getInstance("SHA-256");
      final var hash = md.digest(data);
      return HexFormat.of().formatHex(hash);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}