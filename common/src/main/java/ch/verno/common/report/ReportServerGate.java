package ch.verno.common.report;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ReportServerGate {

  String generateCourseReport(@Nonnull CourseDto course,
                              @Nonnull List<ParticipantDto> participants);

  String generateParticipantsReport();

  ReportDto loadTempFile(@Nonnull String token);

  void deleteTempFile(@Nonnull String token);

  @Nonnull
  String storeFile(@Nonnull String filename,
                   @Nonnull byte[] fileBytes);
}
