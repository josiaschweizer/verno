package ch.verno.contract.endpoint.file;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.dto.file.temp.CsvMapDto;
import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;

@RpcEndpoint
public interface CsvResource {

  @Nonnull
  CsvSchema resolveSchema(@Nonnull String fileToken);

  @Nonnull
  List<CsvMapDto> parseRows(@Nonnull FileDto fileDto);

  @Nonnull
  FileDto parseRows(@Nonnull String filename, @Nonnull List<CsvMapDto> rows);

}
