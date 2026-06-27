package ch.verno.server.io.importing;

import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.file.TempFileBo;
import ch.verno.server.io.importing.csv.CsvSchemaAnalyzer;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;

public class SchemaResolver {

  @Nonnull private final Lazy<TempFileBo> tempFileBo;
  @Nonnull private final CsvSchemaAnalyzer csvSchemaAnalyzer;

  public SchemaResolver(@Nonnull final ServerBean serverBean) {
    this.tempFileBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(TempFileBo.class));
    this.csvSchemaAnalyzer = new CsvSchemaAnalyzer();
  }

  @Nonnull
  public CsvSchema resolveCsvSchema(@Nonnull final String token) {
    final var file = tempFileBo.get().load(token);

    try {
      return csvSchemaAnalyzer.analyze(new ByteArrayInputStream(file.pdfBytes()));
    } catch (final Exception e) {
      throw new RuntimeException("Failed to analyze CSV schema", e);
    }
  }

}
