package ch.verno.server.report;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.report.ReportDto;
import ch.verno.common.report.ReportServerGate;
import ch.verno.server.file.FileStorageHandler;
import ch.verno.server.report.course.CourseReportUseCase;
import ch.verno.server.report.participant.ParticipantReportUseCase;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServerGateImpl implements ReportServerGate {

  @Nonnull private final CourseReportUseCase courseReportUseCase;
  @Nonnull private final ParticipantReportUseCase participantReportUseCase;

  @Nonnull private final FileStorageHandler fileStorageHandler;

  public ReportServerGateImpl(@Nonnull final CourseReportUseCase courseReportUseCase,
                              @Nonnull final ParticipantReportUseCase participantReportUseCase) {
    this.courseReportUseCase = courseReportUseCase;
    this.participantReportUseCase = participantReportUseCase;

    this.fileStorageHandler = new FileStorageHandler();
  }

  @Override
  public String generateCourseReport(@Nonnull final CourseDto course,
                                     @Nonnull final List<ParticipantDto> participants) {
    final var report = courseReportUseCase.generate(course, participants);
    return storeFile(report.filename(), report.pdfBytes());
  }

  @Nonnull
  @Override
  public String generateParticipantsReport() {
    final var report = participantReportUseCase.generate();
    return storeFile(report.filename(), report.pdfBytes());
  }

  @Nonnull
  @Override
  public ReportDto loadTempFile(@Nonnull final String token) {
    return fileStorageHandler.loadTemporaryFile(token);
  }

  @Override
  public void deleteTempFile(@Nonnull final String token) {
    fileStorageHandler.delete(token);
  }

  @Nonnull
  @Override
  public String storeFile(@Nonnull final String filename,
                          @Nonnull final byte[] fileBytes) {
    return fileStorageHandler.storeFileTemporary(filename, fileBytes);
  }
}
