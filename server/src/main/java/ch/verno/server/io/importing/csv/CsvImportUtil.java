package ch.verno.server.io.importing.csv;

import ch.verno.common.exceptions.io.ParseCsvException;
import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;
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

  @Nonnull
  public static byte[] createFileDtoFromRows(@Nonnull final List<CsvMapDto> rows) {
    if (rows.isEmpty()) {
      return new byte[0];
    }

    try (final var out = new ByteArrayOutputStream();
         final var writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
         final var csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

      final var headerMap = rows.getFirst().row();
      final var headers = new ArrayList<>(headerMap.keySet());

      csvPrinter.printRecord(headers);

      for (final var row : rows) {
        final var values = new ArrayList<String>();

        for (final var header : headers) {
          values.add(row.row().get(header));
        }

        csvPrinter.printRecord(values);
      }

      csvPrinter.flush();
      return out.toByteArray();

    } catch (IOException e) {
      throw new ParseCsvException("Failed to create CSV", e);
    }
  }
}