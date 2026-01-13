package ch.verno.report.dto;

import jakarta.annotation.Nonnull;

import java.util.List;

public record ParticipantListReportDto(@Nonnull List<ParticipantReportDto> participantList) {
}
