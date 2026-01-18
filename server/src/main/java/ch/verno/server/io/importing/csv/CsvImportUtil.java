package ch.verno.server.io.importing.csv;

import ch.verno.common.exceptions.io.ParseCsvException;
import ch.verno.common.file.CsvMapDto;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public final class CsvImportUtil {

  private CsvImportUtil() {
  }

  @Nonnull
  public static List<CsvMapDto> parseRows(@Nonnull final byte[] csvBytes) {
    try (final var reader = new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8);
         final var csvParser = CSVFormat.DEFAULT.builder()
                 .setHeader()
                 .setSkipHeaderRecord(true)
                 .setTrim(true)
                 .build()
                 .parse(reader)) {
      final var headers = csvParser.getHeaderNames();
      final var rows = new ArrayList<CsvMapDto>();

      for (final var record : csvParser) {
        final var row = new LinkedHashMap<String, String>();

        for (final var header : headers) {
          row.put(header, record.isMapped(header) ? record.get(header) : null);
        }

        rows.add(new CsvMapDto(row));
      }

      return rows;
    } catch (IOException e) {
      throw new ParseCsvException("Failed to parse CSV", e);
    }
  }
}