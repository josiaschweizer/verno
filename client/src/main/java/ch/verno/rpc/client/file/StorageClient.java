package ch.verno.rpc.client.file;

import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.contract.endpoint.file.StorageResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import jakarta.annotation.Nonnull;

import java.io.InputStream;
import java.util.Optional;

public class StorageClient {

  @Nonnull private final Lazy<StorageResource> storageResource;

  public StorageClient(@Nonnull final RpcFactory rpcFactory) {
    this.storageResource = Lazy.of(() -> rpcFactory.create(StorageResource.class));
  }

  @Nonnull
  public Optional<InputStream> findByKey(@Nonnull final String key) {
    return storageResource.get().findByKey(key);
  }

  public boolean exists(@Nonnull final String key) {
    return storageResource.get().exists(key);
  }

  @Nonnull
  public StoredObjectDto store(@Nonnull final String key,
                               @Nonnull final InputStream data,
                               final long size) {
    return storageResource.get().store(key, data, size);
  }

  public void delete(@Nonnull final Long id) {
    storageResource.get().delete(id);
  }

}
