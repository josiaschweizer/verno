package ch.verno.server.repository.storage;

import ch.verno.db.storage.ObjectStorage;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ObjectStorageService {

  @Nonnull private final Lazy<ObjectStorage> objectStorage;

  public ObjectStorageService(@Nonnull final ServerBean serverBean) {
    this.objectStorage = Lazy.of(() -> serverBean.get(ObjectStorage.class)); //TODO maybe use directly the FileSystemObjectStorage
  }

  public void save(@Nonnull final String storageKey,
                   @Nonnull final InputStream data,
                   final long size) {
    try {
      objectStorage.get().put(storageKey, data, size);
    } catch (final Exception exception) {
      throw new RuntimeException(
              "Could not save file content in object storage.",
              exception
      );
    }
  }

  @Nonnull
  public InputStream get(@Nonnull final String storageKey) {
    try {
      return objectStorage.get().get(storageKey)
              .orElseThrow(() -> new RuntimeException(
                      "File content does not exist in object storage."
              ));
    } catch (final Exception exception) {
      throw new RuntimeException(
              "Could not read file content from object storage.",
              exception
      );
    }
  }

  public void delete(@Nonnull final String storageKey) {
    try {
      objectStorage.get().delete(storageKey);
    } catch (final Exception exception) {
      throw new RuntimeException(
              "Could not delete file content from object storage.",
              exception
      );
    }
  }
}