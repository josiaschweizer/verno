package ch.verno.ui.base.components.calendar;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record WeekCalendarEventDto(@Nonnull String title,
                                   @Nullable String instructor,
                                   @Nullable LocalDateTime start,
                                   @Nullable LocalDateTime end,
                                   @Nullable Long courseId,
                                   @Nonnull String color
) {
}
