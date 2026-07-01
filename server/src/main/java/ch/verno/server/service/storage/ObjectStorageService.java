package ch.verno.server.service.storage;

import ch.verno.db.storage.ObjectStorage;
import ch.verno.lib.exception.ExceptionUtil;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

/**
 * Thin service boundary around {@link ObjectStorage}.
 * Translates checked exceptions into unchecked ones and centralises
 * object-storage access so callers don't depend on the storage provider directly.
 */
@Service
public class ObjectStorageService {

  @Nonnull private final ObjectStorage objectStorage;

  public ObjectStorageService(@Nonnull final ObjectStorage objectStorage) {
    this.objectStorage = objectStorage;
  }

  /**
   * Stores content under the given key.
   *
   * @param key  storage key
   * @param data content byteData
   * @param size content size in bytes
   */
  public void put(@Nonnull final String key,
                  @Nonnull final InputStream data,
                  final long size) {
    try {
      objectStorage.put(key, data, size);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not store object: " + key, exception);
    }
  }

  /**
   * Retrieves content for the given key, if present.
   *
   * @param key storage key
   * @return content byteData, if the object exists
   */
  @Nonnull
  public Optional<InputStream> get(@Nonnull final String key) {
    try {
      return objectStorage.get(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not load object: " + key, exception);
    }
  }

  /**
   * Deletes the object under the given key.
   *
   * @param key storage key
   */
  public void delete(@Nonnull final String key) {
    try {
      objectStorage.delete(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not delete object: " + key, exception);
    }
  }

  /**
   * Checks whether an object exists under the given key.
   *
   * @param key storage key
   * @return {@code true} if the object exists
   */
  public boolean exists(@Nonnull final String key) {
    try {
      return objectStorage.exists(key);
    } catch (final Exception exception) {
      throw ExceptionUtil.toUnchecked("Could not check object existence: " + key, exception);
    }
  }
}