package ch.verno.rpc.client.file;

import ch.verno.contract.endpoint.file.CsvResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class CsvClient {

  @Nonnull private final Lazy<CsvResource> csvResource;

  @Inject
  public CsvClient(@Nonnull final RpcFactory rpcFactory) {
    this.csvResource = Lazy.of(() -> rpcFactory.create(CsvResource.class));
  }

}
