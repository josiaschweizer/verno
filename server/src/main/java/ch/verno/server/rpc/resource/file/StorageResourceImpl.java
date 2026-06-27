package ch.verno.server.rpc.resource.file;

import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.contract.endpoint.file.StorageResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.file.StorageBo;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Optional;

@Component
@RpcResource(StorageResource.class)
public class StorageResourceImpl implements StorageResource {

  @Nonnull private final Lazy<StorageBo> storageBo;

  public StorageResourceImpl(@Nonnull final ServerBean serverBean) {
    this.storageBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(StorageBo.class));
  }

  @Nonnull
  @Override
  public Optional<InputStream> findByKey(@Nonnull final String key) {
    return storageBo.get().getByKey(key);
  }

  @Override
  public boolean exists(@Nonnull final String key) {
    return storageBo.get().exists(key);
  }

  @Nonnull
  @Override
  public StoredObjectDto store(@Nonnull final String key,
                               @Nonnull final InputStream data,
                               final long size) {
    return storageBo.get().save(key, data, size);
  }

  @Override
  public void delete(@Nonnull final Long id) {
    storageBo.get().delete(id);
  }
}
