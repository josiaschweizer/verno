package ch.verno.server.io.importing.csv;

import ch.verno.common.server.io.importing.CsvAnalyzeOptions;
import ch.verno.common.server.io.importing.CsvColumn;
import ch.verno.common.server.io.importing.CsvDetectedType;
import ch.verno.common.server.io.importing.CsvSchema;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class CsvSchemaAnalyzer {

  @Nonnull
  public CsvSchema analyze(@Nonnull final InputStream in) throws IOException {
    return analyze(in, CsvAnalyzeOptions.defaults());
  }

  @Nonnull
  public CsvSchema analyze(@Nonnull final InputStream inputStream,
                           @Nonnull final CsvAnalyzeOptions options) throws IOException {

    Objects.requireNonNull(inputStream, "in");
    Objects.requireNonNull(options, "options");

    try (var reader = new BufferedReader(new InputStreamReader(inputStream, options.charset()))) {

      final String headerLine = readFirstNonEmptyLine(reader);
      if (headerLine == null) {
        return new CsvSchema(List.of());
      }

      final char delimiter = options.delimiter() != null
              ? options.delimiter()
              : detectDelimiter(headerLine);

      final List<String> headerCells = splitCsvLine(headerLine, delimiter, options.quoteChar());

      final List<CsvColumn> columns = new ArrayList<>(headerCells.size());
      for (int i = 0; i < headerCells.size(); i++) {
        final String raw = headerCells.get(i);
        final String name = normalizeHeader(raw, i, options);
        columns.add(new CsvColumn(i, name, CsvDetectedType.UNKNOWN, List.of()));
      }

      if (options.sampleRows() <= 0) {
        return new CsvSchema(List.copyOf(columns));
      }

      final var samplesPerColumn = initSamples(columns.size());

      int rowsRead = 0;
      String line;
      while (rowsRead < options.sampleRows() && (line = reader.readLine()) != null) {
        if (line.isBlank()) {
          continue;
        }

        final var cells = splitCsvLine(line, delimiter, options.quoteChar());
        for (int c = 0; c < columns.size(); c++) {
          final var cell = c < cells.size() ? cells.get(c) : Publ.EMPTY_STRING;
          final var trimmed = cell == null ? Publ.EMPTY_STRING : cell.trim();
          if (!trimmed.isEmpty()) {
            final var bucket = samplesPerColumn.get(c);
            if (bucket.size() < options.maxSamplesPerColumn()) {
              bucket.add(trimmed);
            }
          }
        }

        rowsRead++;
      }

      final var analyzed = new ArrayList<CsvColumn>(columns.size());
      for (int c = 0; c < columns.size(); c++) {
        final var base = columns.get(c);
        final var samples = List.copyOf(samplesPerColumn.get(c));
        final var detectedType = detectType(samples, options);

        analyzed.add(new CsvColumn(base.index(), base.name(), detectedType, samples));
      }

      return new CsvSchema(List.copyOf(analyzed));
    }
  }

  @Nonnull
  private List<List<String>> initSamples(final int columnCount) {
    final List<List<String>> res = new ArrayList<>(columnCount);
    for (int i = 0; i < columnCount; i++) {
      res.add(new ArrayList<>());
    }
    return res;
  }

  @Nullable
  private String readFirstNonEmptyLine(@Nonnull final BufferedReader reader) throws IOException {
    String line;
    while ((line = reader.readLine()) != null) {
      if (!line.isBlank()) {
        return line;
      }
    }

    return null;
  }

  private static String normalizeHeader(@Nullable final String rawHeader,
                                        final int index,
                                        @Nonnull final CsvAnalyzeOptions options) {
    String s = rawHeader == null ? Publ.EMPTY_STRING : rawHeader.trim();

    if (s.length() >= 2 && s.charAt(0) == options.quoteChar() && s.charAt(s.length() - 1) == options.quoteChar()) {
      s = s.substring(1, s.length() - 1).trim();
    }

    if (options.collapseWhitespace()) {
      s = s.replaceAll("\\s+", Publ.SPACE);
    }

    if (s.isEmpty()) {
      s = "column_" + (index + 1);
    }

    return s;
  }

  private char detectDelimiter(@Nullable final String headerLine) {
    final char[] candidates = new char[]{';', ',', '\t', '|'};
    char best = ';';
    int bestCount = -1;

    for (char d : candidates) {
      final int count = splitCsvLine(headerLine, d, '"').size();
      if (count > bestCount) {
        bestCount = count;
        best = d;
      }
    }
    return best;
  }

  @Nonnull
  private List<String> splitCsvLine(@Nullable final String line,
                                    final char delimiter,
                                    final char quoteChar) {
    final var out = new ArrayList<String>();
    if (line == null) {
      return out;
    }

    final var stringBuilder = new StringBuilder();
    boolean inQuotes = false;

    for (int i = 0; i < line.length(); i++) {
      char ch = line.charAt(i);

      if (ch == quoteChar) {
        if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == quoteChar) {
          stringBuilder.append(quoteChar);
          i++;
        } else {
          inQuotes = !inQuotes;
        }
        continue;
      }

      if (ch == delimiter && !inQuotes) {
        out.add(stringBuilder.toString());
        stringBuilder.setLength(0);
        continue;
      }

      stringBuilder.append(ch);
    }

    out.add(stringBuilder.toString());
    return out;
  }

  @Nonnull
  private CsvDetectedType detectType(final List<String> samples,
                                     final CsvAnalyzeOptions options) {
    if (samples == null || samples.isEmpty()) {
      return CsvDetectedType.UNKNOWN;
    }

    boolean allInt = true;
    boolean allDecimal = true;
    boolean allBool = true;
    boolean allDate = true;
    boolean allDateTime = true;

    for (String s : samples) {
      final String v = s == null ? Publ.EMPTY_STRING : s.trim();
      if (v.isEmpty()) {
        continue;
      }

      if (!isInteger(v)) {
        allInt = false;
      }
      if (!isDecimal(v)) {
        allDecimal = false;
      }
      if (!isBoolean(v, options)) {
        allBool = false;
      }
      if (!isDate(v, options.dateFormats())) {
        allDate = false;
      }
      if (!isDateTime(v, options.dateTimeFormats())) {
        allDateTime = false;
      }
    }

    if (allBool) return CsvDetectedType.BOOLEAN;
    if (allInt) return CsvDetectedType.INTEGER;
    if (allDecimal) return CsvDetectedType.DECIMAL;
    if (allDateTime) return CsvDetectedType.DATETIME;
    if (allDate) return CsvDetectedType.DATE;

    return CsvDetectedType.STRING;
  }

  private static boolean isInteger(@Nonnull final String s) {
    int i = 0;
    if (s.startsWith(Publ.MINUS) || s.startsWith(Publ.PLUS)) {
      if (s.length() == 1) {
        return false;
      }

      i = 1;
    }

    for (; i < s.length(); i++) {
      if (!Character.isDigit(s.charAt(i))) {
        return false;
      }
    }

    return true;
  }

  private boolean isDecimal(@Nonnull String s) {
    if (s.startsWith(Publ.MINUS) || s.startsWith(Publ.PLUS)) {
      if (s.length() == 1) {
        return false;
      }

      s = s.substring(1);
    }

    boolean seenSep = false;
    for (int i = 0; i < s.length(); i++) {
      final var c = s.charAt(i);

      if (Character.isDigit(c)) {
        continue;
      }

      if ((c == '.' || c == ',') && !seenSep) {
        seenSep = true;
        continue;
      }
      return false;
    }
    return true;
  }

  private boolean isBoolean(@Nonnull final String v,
                            @Nonnull final CsvAnalyzeOptions options) {
    final String s = v.trim().toLowerCase(Locale.ROOT);
    return options.trueTokens().contains(s) || options.falseTokens().contains(s);
  }

  private boolean isDate(@Nonnull final String v,
                         @Nonnull final List<DateTimeFormatter> formats) {
    if (tryParseDate(v, DateTimeFormatter.ISO_LOCAL_DATE)) return true;
    for (DateTimeFormatter f : formats) {
      if (tryParseDate(v, f)) return true;
    }
    return false;
  }

  private boolean tryParseDate(@Nonnull final String v,
                               @Nonnull final DateTimeFormatter f) {
    try {
      LocalDate.parse(v, f);
      return true;
    } catch (DateTimeParseException ignored) {
      return false;
    }
  }

  private boolean isDateTime(@Nonnull final String s,
                             @Nonnull final List<DateTimeFormatter> formats) {
    if (tryParseLocalDateTime(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)) {
      return true;
    } else if (tryParseOffsetDateTime(s)) {
      return true;
    }

    for (DateTimeFormatter f : formats) {
      if (tryParseLocalDateTime(s, f)) {
        return true;
      }
    }

    return false;
  }

  private boolean tryParseLocalDateTime(@Nonnull final String s,
                                        @Nonnull final DateTimeFormatter formatter) {
    try {
      LocalDateTime.parse(s, formatter);
      return true;
    } catch (DateTimeParseException ignored) {
      return false;
    }
  }

  private boolean tryParseOffsetDateTime(@Nonnull final String s) {
    try {
      OffsetDateTime.parse(s);
      return true;
    } catch (DateTimeParseException ignored) {
      return false;
    }
  }
}