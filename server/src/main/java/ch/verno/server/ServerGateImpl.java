package ch.verno.server;

import ch.verno.common.file.FileServerGate;
import ch.verno.common.server.ServerGate;
import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.server.io.importing.SchemaResolver;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServerGateImpl implements ServerGate {

  @Nonnull private final FileServerGate fileServerGate;

  public ServerGateImpl(@Nonnull final FileServerGate fileServerGate) {
    this.fileServerGate = fileServerGate;
  }

  @Nonnull
  @Override
  public CsvSchema resolveCsvSchema(@Nonnull final String fileToken) {
    final var schemaResolver = new SchemaResolver(fileServerGate);
    return schemaResolver.resolveCsvSchema(fileToken);
  }

  @Override
  public void importCsvData(@NonNull final String fileToken, @NonNull final Map<String, String> mapping) {

  }

}
