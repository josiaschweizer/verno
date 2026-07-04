package ch.verno.server.rpc.resource.file;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.contract.dto.file.temp.CsvMapDto;
import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.endpoint.file.CsvResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.io.importing.SchemaResolver;
import ch.verno.server.io.importing.csv.CsvImportUtil;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RpcResource(CsvResource.class)
public class CsvResourceImpl implements CsvResource {

  @Nonnull private final Lazy<SchemaResolver> schemaResolver;

  public CsvResourceImpl(@Nonnull final ServerBean serverBean) {
    this.schemaResolver = Lazy.of(() -> new SchemaResolver(serverBean));
  }

  @Nonnull
  @Override
  public CsvSchema resolveSchema(@Nonnull final String fileToken) {
    return schemaResolver.get().resolveCsvSchema(fileToken);
  }

  @Nonnull
  @Override
  public List<CsvMapDto> parseRows(@Nonnull final FileDto fileDto) {
    return List.of();
  }

  @Nonnull
  @Override
  public FileDto parseRows(@Nonnull final String filename,
                           @Nonnull final List<CsvMapDto> rows) {
    return new FileDto(filename, CsvImportUtil.createFileDtoFromRows(rows));
  }
}
