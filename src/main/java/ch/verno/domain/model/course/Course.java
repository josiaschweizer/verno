package ch.verno.domain.model.course;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.Instant;

public record Course(
    long id,
    @Nullable String title,
    @Nullable Integer capacity,
    @Nullable String location,
    @Nullable Integer durationMinutes,
    @Nullable Long courseLevelId,
    @Nullable Long instructorId,
    @Nullable Long courseUnitId,
    @Nonnull Instant createdAt
) {
}