package ch.verno.ui.base.components.calendar;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;

public record WeekCalendarEventDto(@Nonnull String title,
                                   @Nonnull LocalDateTime start,
                                   @Nonnull LocalDateTime end,
                                   @Nullable Long courseId
) {
}
