package ch.verno.server.report.course;

import ch.verno.common.api.dto.internal.file.temp.FileDto;
import ch.verno.common.db.dto.file.FileDownload;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.service.IFileStorageService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.common.gate.GlobalInterface;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseReportUseCase {

  @Nonnull private final IParticipantService participantService;
  @Nonnull private final ITenantSettingService tenantSettingService;
  @Nonnull private final IFileStorageService fileStorageService;
  @Nonnull private final CourseReportRenderer courseReportRenderer;

  public CourseReportUseCase(@Nonnull final GlobalInterface globalInterface,
                             @Nonnull final CourseReportRenderer courseReportRenderer) {
    this.participantService = globalInterface.getService(IParticipantService.class);
    this.tenantSettingService = globalInterface.getService(ITenantSettingService.class);
    this.fileStorageService = globalInterface.getService(IFileStorageService.class);
    this.courseReportRenderer = courseReportRenderer;
  }

  @Nonnull
  public FileDto generate(@Nonnull final CourseDto course) {
    final var participants = participantService.findParticipantsByCourse(course);
    return generate(course, participants);
  }

  @Nonnull
  public FileDto generate(@Nonnull final CourseDto course,
                          @Nonnull final List<ParticipantDto> participants) {
    final var courseDates = new ArrayList<LocalDate>(); // TODO aus Schedules ableiten
    final var reportData = CourseReportMapper.map(course, participants, courseDates);

    final var settings = tenantSettingService.getCurrentTenantSettingOrDefault();

    byte[] templateBytes = null;
    if (settings.getCourseReportTemplate() != null) {
      final var templateDownload = fileStorageService.download(settings.getCourseReportTemplate());
      templateBytes = getTemplateBytes(templateDownload);
    }

    final var pdfBytes = courseReportRenderer.renderReportPdf(reportData, templateBytes);
    final var filename = settings.getCourseReportName().toLowerCase() + "_" + course.getTitle().toLowerCase() + ".pdf";
    return new FileDto(filename, pdfBytes);
  }

  @Nonnull
  private byte[] getTemplateBytes(@Nonnull final FileDownload templateDownload) {
    try (final var inputStream = templateDownload.stream()) {
      if (inputStream != null) {
        return inputStream.readAllBytes();
      } else {
        throw new RuntimeException("Template file not found");
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load template file", e);
    }
  }
}