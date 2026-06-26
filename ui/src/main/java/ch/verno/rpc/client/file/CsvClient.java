package ch.verno.rpc.client.file;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.dto.file.temp.CsvMapDto;
import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.endpoint.file.CsvResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.List;

public class CsvClient {

  @Nonnull private final Lazy<CsvResource> csvResource;

  @Inject
  public CsvClient(@Nonnull final RpcFactory rpcFactory) {
    this.csvResource = Lazy.of(() -> rpcFactory.create(CsvResource.class));
  }

  @Nonnull
  public CsvSchema resolveCsvSchema(@Nonnull final String fileToken) {
    return csvResource.get().resolveSchema(fileToken);
  }

  @Nonnull
  public FileDto generateFileFromCsv(@Nonnull final String filename,
                                     @Nonnull final List<CsvMapDto> rows) {
    return csvResource.get().generateFileFromCsv(filename, rows);
  }

  @Nonnull
  public List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto) {
    return csvResource.get().parseRows(fileDto);
  }

  @Nonnull
  public FileDto parseRows(@Nonnull final String filename,
                           @Nonnull final List<CsvMapDto> rows) {
    return csvResource.get().parseRows(rows, filename);
  }

}
