package ch.verno.rpc.client.file;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class TempFileClient {

  @Nonnull private final Lazy<TempFileResource> tempFileResource;

  @Inject
  public TempFileClient(@Nonnull final RpcFactory rpcFactory) {
    this.tempFileResource = Lazy.of(() -> rpcFactory.create(TempFileResource.class));
  }

  @Nonnull
  public FileDto loadFile(@Nonnull final String token) {
    return tempFileResource.get().loadFile(token);
  }

  @Nonnull
  public String store(@Nonnull final String filename,
                      @Nonnull final byte[] fileBytes) {
    return tempFileResource.get().store(filename, fileBytes);
  }

  @Nonnull
  public String store(@Nonnull final FileDto file) {
    return tempFileResource.get().store(file);
  }

  public void delete(@Nonnull final String token) {
    tempFileResource.get().delete(token);
  }

}
