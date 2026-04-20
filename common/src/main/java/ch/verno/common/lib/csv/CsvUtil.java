package ch.verno.common.lib.csv;

import ch.verno.common.server.io.importing.CsvDelimiter;
import ch.verno.lib.StringSanitizer;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

import java.nio.charset.StandardCharsets;

public class CsvUtil {

  private CsvUtil() {
  }

  @Nonnull
  public static CsvDelimiter detectDelimiter(@Nonnull final byte[] csvBytes) {
    final var content = new String(csvBytes, StandardCharsets.UTF_8);
    final var lines = content.lines()
            .map(StringSanitizer::clean)
            .filter(line -> line != null && !line.isBlank())
            .limit(5)
            .toList();

    long semicolons = 0;
    long commas = 0;

    for (final var line : lines) {
      semicolons += line.chars().filter(c -> c == Publ.Char.SEMICOLON).count();
      commas += line.chars().filter(c -> c == Publ.Char.COMMA).count();
    }

    final var delimiterChar = semicolons > commas ? Publ.Char.SEMICOLON : Publ.Char.COMMA;
    return CsvDelimiter.fromCharacter(delimiterChar);
  }

}
