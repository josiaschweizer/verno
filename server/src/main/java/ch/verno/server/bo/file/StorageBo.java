package ch.verno.server.bo.file;

import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.contract.dto.table.file.FileDownload;
import ch.verno.contract.dto.table.file.FileUploadDto;
import ch.verno.contract.dto.table.file.StoredFileDto;
import ch.verno.db.storage.ObjectStorage;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.exception.ExceptionUtil;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.file.StoredFileService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Optional;

@Component
public class StorageBo {

  @NonNls public static final String FILE_BIN = "file.bin";
  @NonNls public static final String FILES = "files";
  @NonNls public static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";

  @Nonnull private final Lazy<ObjectStorage> objectStorage;
  @Nonnull private final Lazy<StoredFileService> storedFileService;

  protected StorageBo(@Nonnull final ServerBean serverBean) {
    this.storedFileService = Lazy.of(() -> serverBean.get(StoredFileService.class));
    this.objectStorage = Lazy.of(() -> serverBean.get(ObjectStorage.class));
  }

  /**
   * Stores file metadata in the database and its content in the object storage.
   *
   * @param file uploaded file
   * @return stored file metadata
   */
  @Nonnull
  @Transactional
  public StoredFileDto save(@Nonnull final FileUploadDto file) {
    if (file.byteContent().length == 0) {
      throw new IllegalArgumentException("File is empty.");
    }

    final var dto = StoredFileDto.empty();
    dto.setFilename(safeFilename(file.filename()));
    dto.setContentType(normalizeContentType(file.contentType()));
    dto.setSize((long) file.byteContent().length);
    dto.setChecksumSha256(sha256Hex(file.byteContent()));

    final var storedFile = storedFileService.get().save(dto);
    final var storageKey = buildStorageKey(storedFile.getId());

    try {
      save(storageKey, new ByteArrayInputStream(file.byteContent()), file.byteContent().length);
      storedFileService.get().setStorageKey(storedFile.getId(), storageKey);

      return storedFile;
    } catch (final RuntimeException exception) {
      storedFileService.get().deleteById(storedFile.getId());
      throw exception;
    }
  }

  /**
   * Stores content directly in the object storage.
   *
   * @param key  object storage key
   * @param data file content
   * @param size file size in bytes
   * @return stored object information
   */
  @Nonnull
  public StoredObjectDto save(@Nonnull final String key,
                              @Nonnull final InputStream data,
                              final long size) {
    try {
      final var storedObject = objectStorage.get().put(key, data, size);
      return new StoredObjectDto(storedObject.key(), storedObject.size());
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not store object.", exception);
    }
  }

  /**
   * Returns metadata for a stored file.
   *
   * @param id stored file ID
   * @return stored file metadata, if available - else returns an empty StoredFileDto
   */
  @Nonnull
  @Transactional(readOnly = true)
  public StoredFileDto getMeta(@Nonnull final Long id) {
    final var meta = getMetaById(id);
    return meta.orElseGet(StoredFileDto::empty);
  }

  /**
   * Returns metadata for a stored file.
   *
   * @param id stored file ID
   * @return stored file metadata, if available
   */
  @Nonnull
  @Transactional(readOnly = true)
  public Optional<StoredFileDto> getMetaById(@Nonnull final Long id) {
    return storedFileService.get().findById(id);
  }

  /**
   * Loads file content using the stored file ID.
   *
   * @param id stored file ID
   * @return file content as input byteData
   */
  @Nonnull
  @Transactional(readOnly = true)
  public InputStream getContent(@Nonnull final Long id) {
    final var storageKey = getStorageKey(id);

    try {
      return objectStorage.get().get(storageKey).orElseThrow(() -> new IllegalStateException("File content not found in object storage: " + id));
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not load file content.", exception);
    }
  }

  /**
   * Loads file content directly from the object storage.
   *
   * @param key object storage key
   * @return file content, if available
   */
  @Nonnull
  public Optional<InputStream> getByKey(@Nonnull final String key) {
    try {
      return objectStorage.get().get(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not load object.", exception);
    }
  }

  /**
   * Loads the file metadata and, if available, its content.
   *
   * @param id stored file ID
   * @return download result with metadata and optional content
   */
  @Nonnull
  @Transactional(readOnly = true)
  public FileDownload download(@Nonnull final Long id) {
    final var storedFile = getMetaById(id);

    if (storedFile.isEmpty()) {
      return FileDownload.empty();
    }

    final var storageKey = storedFileService.get().getStorageKey(id);
    if (storageKey.isBlank()) {
      return FileDownload.noContent(storedFile.get());
    }

    try {
      final var content = objectStorage.get().get(storageKey);

      return content
              .map(inputStream -> new FileDownload(storedFile.get(), getBytesFromInputStream(inputStream)))
              .orElseGet(() -> FileDownload.noContent(storedFile.get()));
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not download file.", exception);
    }
  }

  /**
   * Checks whether an object exists in the object storage.
   *
   * @param key object storage key
   * @return {@code true} if the object exists
   */
  public boolean exists(@Nonnull final String key) {
    try {
      return objectStorage.get().exists(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not check object existence.", exception);
    }
  }

  /**
   * Deletes a stored file and its associated object storage content.
   *
   * @param id stored file ID
   */
  @Transactional
  public void delete(@Nonnull final Long id) {
    final var objectStorageKey = storedFileService.get().getStorageKey(id);

    try {
      if (!objectStorageKey.isBlank()) {
        delete(objectStorageKey);
      }

      storedFileService.get().deleteById(id);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not delete file.", exception);
    }
  }

  /**
   * Deletes an object directly from the object storage.
   *
   * @param key object storage key
   */
  public void delete(@Nonnull final String key) {
    try {
      objectStorage.get().delete(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not delete object.", exception);
    }
  }

  @Nonnull
  private String getStorageKey(@Nonnull final Long id) {
    final var storageKey = storedFileService.get().getStorageKey(id);

    if (storageKey.isBlank()) {
      throw new IllegalStateException("File metadata exists without storage key: " + id);
    }

    return storageKey;
  }

  @Nonnull
  private static String buildStorageKey(@Nonnull final Long id) {
    final var idString = id.toString();
    final var prefix = idString.length() >= 2
            ? idString.substring(0, 2)
            : "0" + idString;

    return FILES
            + Publ.SLASH
            + prefix
            + Publ.SLASH
            + idString;
  }

  @Nonnull
  private static String safeFilename(@Nullable final String filename) {
    if (filename == null || filename.isBlank()) {
      return FILE_BIN;
    }

    var safeFilename = filename.replace("\\", Publ.SLASH);
    safeFilename = safeFilename.substring(safeFilename.lastIndexOf(Publ.SLASH) + 1);
    safeFilename = safeFilename.replaceAll("[\\r\\n\\t]", Publ.UNDERSCORE).trim();

    return safeFilename.isBlank() ? FILE_BIN : safeFilename;
  }

  @Nonnull
  private static String normalizeContentType(@Nullable final String contentType) {
    return contentType == null || contentType.isBlank()
            ? DEFAULT_CONTENT_TYPE
            : contentType;
  }

  @Nonnull
  private static String sha256Hex(@Nonnull final byte[] data) {
    try {
      final var messageDigest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(messageDigest.digest(data));
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not calculate checksum.", exception);
    }
  }

  @Nonnull
  private byte[] getBytesFromInputStream(@Nonnull final InputStream inputStream) {
    try {
      return inputStream.readAllBytes();
    } catch (Exception e) {
      throw new RuntimeException("Failed to load template file", e);
    }
  }
}