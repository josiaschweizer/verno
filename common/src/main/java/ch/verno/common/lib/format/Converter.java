package ch.verno.common.lib.format;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Converter {

  @Nonnull
  public static String localDateTime(@Nonnull final LocalDateTime localDateTime) {
    final var formatter = DateTimeFormatter.ofPattern(ConverterConstants.LOCAL_DATE_TIME_DEFAULT_FORMAT);
    return localDateTime.format(formatter);
  }

}
