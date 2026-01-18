package ch.verno.server.io.importing;

import ch.verno.common.file.FileServerGate;
import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.server.io.importing.csv.CsvSchemaAnalyzer;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.util.Map;

public class SchemaResolver {

  @Nonnull private final FileServerGate fileServerGate;
  @Nonnull private final CsvSchemaAnalyzer csvSchemaAnalyzer;

  public SchemaResolver(@Nonnull final FileServerGate fileServerGate) {
    this.fileServerGate = fileServerGate;
    this.csvSchemaAnalyzer = new CsvSchemaAnalyzer();
  }

  public CsvSchema resolveCsvSchema(@Nonnull final String token) {
    final var file = fileServerGate.loadFile(token);

    try {
      return csvSchemaAnalyzer.analyze(new ByteArrayInputStream(file.pdfBytes()));
    } catch (final Exception e) {
      throw new RuntimeException("Failed to analyze CSV schema", e);
    }
  }

}
