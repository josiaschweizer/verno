package ch.verno.report.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CourseReportDto(@Nonnull String title,
                              @Nonnull List<ParticipantReportDto> participants,
                              @Nullable Integer capacity,
                              @Nonnull String courseLevels,
                              @Nonnull String courseSchedule,
                              @Nonnull List<LocalDate> courseDates,
                              @Nullable LocalTime startTime,
                              @Nullable LocalTime endTime) {
}
