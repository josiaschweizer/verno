package ch.verno.rpc.client.io;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.endpoint.io.ImportResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

public class ImportClient {

  @Nonnull private final Lazy<ImportResource> importResource;

  @Inject
  public ImportClient(@Nonnull final RpcFactory rpcFactory) {
    this.importResource = Lazy.of(() -> rpcFactory.create(ImportResource.class));
  }

  @Nonnull
  public CsvSchema resolveCsvSchema(@Nonnull final String fileToken) {
    return importResource.get().resolveCsvSchema(fileToken);
  }

}
