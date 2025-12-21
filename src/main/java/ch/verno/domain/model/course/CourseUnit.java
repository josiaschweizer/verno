package ch.verno.domain.model.course;

import jakarta.annotation.Nonnull;

import java.time.Instant;
import java.util.List;

public record CourseUnit(
    long id,
    @Nonnull String name,
    @Nonnull List<CourseUnitWeek> weeks,
    @Nonnull Instant createdAt
) {
}