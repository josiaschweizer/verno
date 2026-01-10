package ch.verno.report.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record ParticipantReportDto(@Nonnull String firstname,
                                   @Nonnull String lastname,
                                   @Nullable LocalDate birthdate,
                                   @Nonnull String gender,
                                   @Nonnull String parentOne,
                                   @Nonnull String parentTwo,
                                   @Nonnull String address,
                                   @Nonnull String email,
                                   @Nonnull String phone,
                                   @Nonnull String courseLevels,
                                   @Nonnull String note) {
}
