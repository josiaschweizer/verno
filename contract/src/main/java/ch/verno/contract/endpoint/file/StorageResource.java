package ch.verno.contract.endpoint.file;

import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.io.InputStream;
import java.util.Optional;

@RpcEndpoint
public interface StorageResource {

  @Nonnull
  Optional<InputStream> findByKey(@Nonnull String key);

  boolean exists(@Nonnull String key);

  @Nonnull
  StoredObjectDto store(@Nonnull String key,
                        @Nonnull InputStream data,
                        long size);

  void delete(@Nonnull Long id);
}
