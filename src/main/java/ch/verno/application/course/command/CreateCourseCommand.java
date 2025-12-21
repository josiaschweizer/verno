package ch.verno.application.course.command;

import jakarta.annotation.Nullable;

public record CreateCourseCommand(
    @Nullable String title,
    @Nullable Integer capacity,
    @Nullable String location,
    @Nullable Integer durationMinutes,
    @Nullable Long courseLevelId,
    @Nullable Long instructorId,
    @Nullable Long courseUnitId
) {
}