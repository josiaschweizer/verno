package ch.verno.server;

import ch.verno.common.gate.servergate.TempFileServerGate;
import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.api.dto.internal.file.temp.FileDto;
import ch.verno.common.gate.servergate.ServerGate;
import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.server.io.importing.SchemaResolver;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerGateImpl implements ServerGate {

  @Nonnull private final TempFileServerGate tempFileServerGate;

  public ServerGateImpl(@Nonnull final TempFileServerGate tempFileServerGate) {
    this.tempFileServerGate = tempFileServerGate;
  }

  @Nonnull
  @Override
  public CsvSchema resolveCsvSchema(@Nonnull final String fileToken) {
    final var schemaResolver = new SchemaResolver(tempFileServerGate);
    return schemaResolver.resolveCsvSchema(fileToken);
  }

  @Nonnull
  @Override
  public FileDto generateFileFromCsv(@Nonnull final String filename,
                                     @NonNull final List<CsvMapDto> rows) {
    return tempFileServerGate.parseRows(rows, filename);
  }

}
