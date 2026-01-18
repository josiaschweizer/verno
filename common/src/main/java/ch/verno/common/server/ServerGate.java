package ch.verno.common.server;

import ch.verno.common.server.io.importing.CsvSchema;
import jakarta.annotation.Nonnull;

import java.util.Map;

public interface ServerGate {

  @Nonnull
  CsvSchema resolveCsvSchema(@Nonnull String fileToken);

  void importCsvData(@Nonnull final String fileToken,
                     @Nonnull final Map<String, String> mapping);
}
