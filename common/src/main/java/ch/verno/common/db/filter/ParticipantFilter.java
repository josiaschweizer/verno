package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record ParticipantFilter(@Nullable String searchText,
                                @Nullable Long genderId,
                                @Nullable Long courseLevelId,
                                @Nullable Long instructorId,
                                @Nullable Long courseId,
                                @Nullable LocalDate birthDateFrom,
                                @Nullable LocalDate birthDateTo) {

  @Nonnull
  public static ParticipantFilter fromSearchText(@Nullable final String searchText) {
    return new ParticipantFilter(searchText, null, null, null, null, null, null);
  }

  @Nonnull
  public static ParticipantFilter empty() {
    return new ParticipantFilter(null, null, null, null, null, null, null);
  }

}
