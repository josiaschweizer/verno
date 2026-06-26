package ch.verno.server.bo.file;

import ch.verno.contract.dto.file.storage.DeletedObjectDto;
import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.db.storage.ObjectStorage;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

import java.io.InputStream;
import java.util.Optional;

public class StorageBo {

  @Nonnull private final Lazy<ObjectStorage> objectStorage;

  protected StorageBo(@Nonnull final ServerBean serverBean) {
    this.objectStorage = Lazy.of(() -> serverBean.get(ObjectStorage.class));
  }

  @Nonnull
  public Optional<InputStream> findByKey(@Nonnull final String key) {
    try {
      return objectStorage.get().get(key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public boolean exists(@Nonnull final String key) {
    try {
      return objectStorage.get().exists(key);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public StoredObjectDto save(@Nonnull final String key,
                              @Nonnull final InputStream data,
                              final long size) {
    try {
      final var saveResult = objectStorage.get().put(key, data, size);
      return new StoredObjectDto(saveResult.key(), saveResult.size());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Nonnull
  public DeletedObjectDto delete(@Nonnull final String key) {
    try {
      objectStorage.get().delete(key);
      return DeletedObjectDto.successfully();
    } catch (Exception e) {
      return DeletedObjectDto.faulty();
    }
  }


}