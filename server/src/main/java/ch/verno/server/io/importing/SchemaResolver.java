package ch.verno.server.io.importing;

import ch.verno.common.gate.servergate.TempFileServerGate;
import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.server.io.importing.csv.CsvSchemaAnalyzer;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;

public class SchemaResolver {

  @Nonnull private final TempFileServerGate tempFileServerGate;
  @Nonnull private final CsvSchemaAnalyzer csvSchemaAnalyzer;

  public SchemaResolver(@Nonnull final TempFileServerGate tempFileServerGate) {
    this.tempFileServerGate = tempFileServerGate;
    this.csvSchemaAnalyzer = new CsvSchemaAnalyzer();
  }

  public CsvSchema resolveCsvSchema(@Nonnull final String token) {
    final var file = tempFileServerGate.loadFile(token);

    try {
      return csvSchemaAnalyzer.analyze(new ByteArrayInputStream(file.pdfBytes()));
    } catch (final Exception e) {
      throw new RuntimeException("Failed to analyze CSV schema", e);
    }
  }

}
