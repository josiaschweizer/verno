package ch.verno.contract.endpoint.file;

import ch.verno.contract.dto.file.storage.StoredObjectDto;
import ch.verno.contract.dto.table.file.FileDownload;
import ch.verno.contract.dto.table.file.FileUploadDto;
import ch.verno.contract.dto.table.file.StoredFileDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.io.InputStream;
import java.util.Optional;

@RpcEndpoint
public interface StorageResource {

  @Nonnull
  Optional<InputStream> findByKey(@Nonnull String key);

  @Nonnull
  FileDownload download(@Nonnull Long id);

  boolean exists(@Nonnull String key);

  @Nonnull
  StoredFileDto upload(@Nonnull FileUploadDto file);

  @Nonnull
  StoredObjectDto store(@Nonnull String key,
                        @Nonnull InputStream data,
                        long size);

  void delete(@Nonnull Long id);

  @Nonnull
  StoredFileDto getMeta(@Nonnull Long id);
}
