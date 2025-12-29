package ch.verno.ui.base.components.calendar;

import jakarta.annotation.Nonnull;

import java.time.LocalDateTime;

public record WeekCalendarEvent(@Nonnull String title,
                                @Nonnull LocalDateTime start,
                                @Nonnull LocalDateTime end) {
}
