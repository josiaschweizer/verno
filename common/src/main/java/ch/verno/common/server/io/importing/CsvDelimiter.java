package ch.verno.common.server.io.importing;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

public enum CsvDelimiter {
  COMMA(Publ.Char.COMMA),
  SEMICOLON(Publ.Char.SEMICOLON),
  ;

  private final char character;

  CsvDelimiter(final char character) {
    this.character = character;
  }

  @Nonnull
  public static CsvDelimiter fromCharacter(final char character) {
    for (final var value : values()) {
      if (value.getCharacter() == character) {
        return value;
      }
    }

    return CsvDelimiter.COMMA;
  }

  public char getCharacter() {
    return character;
  }

  @Nonnull
  public String getCharacterString() {
    return toString();
  }

  @Nonnull
  @Override
  public String toString() {
    return String.valueOf(character);
  }

}
