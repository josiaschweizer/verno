package ch.verno.common.lib.format;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {

  @Nonnull
  public static String localDateTime(@Nullable final LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return Publ.EMPTY_STRING;
    }

    final var formatter = DateTimeFormatter.ofPattern(ConverterConstants.LOCAL_DATE_TIME_DEFAULT_FORMAT);
    return localDateTime.format(formatter);
  }

}
