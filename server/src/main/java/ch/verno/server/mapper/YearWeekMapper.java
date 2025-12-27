package ch.verno.server.mapper;

import ch.verno.common.db.dto.YearWeekDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class YearWeekMapper {

  @Nonnull
  public static List<String> mapWeeksToStrings(@Nullable final List<YearWeekDto> weeks) {
    if (weeks == null || weeks.isEmpty()) {
      return List.of();
    }

    final var result = new ArrayList<String>(weeks.size());
    for (final var yw : weeks) {
      if (yw == null) continue;
      result.add(yw.toString());
    }
    return result;
  }

  @Nonnull
  public static List<YearWeekDto> mapWeeksToYearWeeks(@Nullable final List<String> weeks) {
    if (weeks == null || weeks.isEmpty()) {
      return List.of();
    }

    final var result = new ArrayList<YearWeekDto>(weeks.size());
    for (final var w : weeks) {
      final var yw = parseYearWeek(w);
      if (yw != null) {
        result.add(yw);
      }
    }
    return result;
  }

  @Nullable
  public static YearWeekDto parseYearWeek(@Nullable final String value) {
    if (value == null || value.isBlank()) {
      return null;
    }

    final Matcher m = Publ.KW_PATTERN.matcher(value.trim());
    if (!m.matches()) {
      return null;
    }

    final int week = Integer.parseInt(m.group(1));
    final int year = Integer.parseInt(m.group(2));

    if (week < 1 || week > 53) {
      return null;
    }

    return new YearWeekDto(year, week);
  }

}
