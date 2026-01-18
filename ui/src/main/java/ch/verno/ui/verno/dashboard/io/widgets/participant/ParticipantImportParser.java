package ch.verno.ui.verno.dashboard.io.widgets.participant;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class ParticipantImportParser {

  private static final DateTimeFormatter[] DATE_FORMATS = new DateTimeFormatter[]{
          DateTimeFormatter.ISO_LOCAL_DATE,
          DateTimeFormatter.ofPattern("dd.MM.yyyy")
  };

  private ParticipantImportParser() {
  }

  @Nullable
  public static LocalDate parseDate(@Nonnull final String raw) {
    final var trimmed = raw.trim();
    if (trimmed.isEmpty()) return null;

    for (var fmt : DATE_FORMATS) {
      try {
        return LocalDate.parse(trimmed, fmt);
      } catch (DateTimeParseException ignored) {

      }
    }
    throw new IllegalArgumentException("Invalid date: '" + raw + "'");
  }
}