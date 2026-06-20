package ch.verno.rpc.client.file;

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

}
