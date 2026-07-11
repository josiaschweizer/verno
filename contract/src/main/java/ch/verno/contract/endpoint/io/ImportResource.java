package ch.verno.contract.endpoint.io;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface ImportResource {

  @Nonnull
  CsvSchema resolveCsvSchema(@Nonnull String fileToken);

}
